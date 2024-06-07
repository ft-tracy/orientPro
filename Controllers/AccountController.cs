using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using System.Threading.Tasks;
using LoginApp.Data;
using LoginApp.Models;
using LoginApp.Services;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Authorization;
using Microsoft.Extensions.Logging;
using Microsoft.Extensions.Configuration;
using System;
using System.Security.Claims;
using Microsoft.IdentityModel.Tokens;
using System.Text;
using Microsoft.IdentityModel.JsonWebTokens;

namespace LoginApp.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class AccountController : ControllerBase
    {
        private readonly ApplicationDbContext _context;
        private readonly IPasswordHasher<User> _passwordHasher;
        private readonly UserService _userService;
        private readonly ILogger<AccountController> _logger;
        private readonly IConfiguration _configuration;

        public AccountController(ApplicationDbContext context, IPasswordHasher<User> passwordHasher, UserService userService, ILogger<AccountController> logger, IConfiguration configuration)
        {
            _context = context;
            _passwordHasher = passwordHasher;
            _userService = userService;
            _logger = logger;
            _configuration = configuration;
        }

        [AllowAnonymous]
        [HttpPost("Login")]
        public async Task<IActionResult> Login([FromBody] LoginRequest request)
        {
            _logger.LogInformation("Login request received for email: {Email}", request.Email);

            var user = await _context.Users.SingleOrDefaultAsync(u => u.Email == request.Email);

            if (user == null)
            {
                _logger.LogWarning("Login failed for email: {Email} - User not found", request.Email);
                return Unauthorized(new { message = "Invalid credentials." });
            }

            bool isValidUser = false;
            bool isFirstLogin = user.IsFirstLogin;

            if (isFirstLogin)
            {
                if (user.Role == "Admin" && request.Email == user.Email && request.Password == user.Pwd)
                {
                    isValidUser = true;
                }
                else if ((user.Role == "ContentManager" || user.Role == "Trainee") &&
                         request.Password == user.OTP && request.Email == user.Email && user.OTPExpiration >= DateTime.Now)
                {
                    isValidUser = true;
                }

                if (!isValidUser)
                {
                    _logger.LogWarning("First login failed for {Role} email: {Email} - Invalid credentials", user.Role, request.Email);
                    return Unauthorized(new { message = "Invalid credentials." });
                }
            }
            else
            {
                if (_passwordHasher.VerifyHashedPassword(user, user.Pwd, request.Password) == PasswordVerificationResult.Failed)
                {
                    _logger.LogWarning("Subsequent login failed for email: {Email} - Invalid password", request.Email);
                    return Unauthorized(new { message = "Invalid credentials." });
                }
                isValidUser = true;
            }

            if (isValidUser)
            {
                var tokenString = GenerateJwtToken(user);
                _logger.LogInformation("Login success for email: {Email}", request.Email);

                return Ok(new { token = tokenString, isFirstLogin = isFirstLogin });
            }

            return Unauthorized(new { message = "Invalid credentials." });
        }

        private string GenerateJwtToken(User user)
        {
            var jwtKey = _configuration["Jwt:Key"];
            if (string.IsNullOrEmpty(jwtKey))
            {
                _logger.LogError("JWT Key is not configured.");
                throw new Exception("JWT Key is not configured.");
            }

            _logger.LogDebug("JWT Key for token generation: {Key}", jwtKey);

            var tokenHandler = new JsonWebTokenHandler();
            var key = Encoding.ASCII.GetBytes(jwtKey);
            var signingKey = new SymmetricSecurityKey(key);
            var signingCredentials = new SigningCredentials(signingKey, SecurityAlgorithms.HmacSha256);

            var claims = new[]
     {
        new Claim(ClaimTypes.Name, user.Email),
        new Claim(ClaimTypes.Role, user.Role),
        new Claim(ClaimTypes.NameIdentifier, user.UniqueID.ToString()) // Assuming UniqueID is the user's identifier in the database
    };

            var tokenDescriptor = new SecurityTokenDescriptor
            {
                Subject = new ClaimsIdentity(claims),
                Expires = DateTime.UtcNow.AddHours(8),
                SigningCredentials = signingCredentials,
                Audience = "https://orientpro.onrender.com", // Add this line
                Issuer = "https://04a3-41-90-101-26.ngrok-free.app" // Add this line
            };

            var tokenString = tokenHandler.CreateToken(tokenDescriptor);

            _logger.LogDebug("Generated JWT Token: {Token}", tokenString);
            return tokenString;
        }



        [AllowAnonymous]
        [HttpPost("ResetPassword")]
        public async Task<IActionResult> ResetPassword([FromBody] ResetPasswordRequest request)
        {
            _logger.LogInformation("Reset password request received for email: {Email}", request.Email);

            var user = await _context.Users.SingleOrDefaultAsync(u => u.Email == request.Email);
            if (user == null)
            {
                _logger.LogWarning("Reset password failed for email: {Email} - User not found", request.Email);
                return NotFound(new { message = "User not found." });
            }

            if (request.NewPassword != request.ConfirmPassword)
            {
                _logger.LogWarning("Reset password failed for email: {Email} - Passwords do not match", request.Email);
                return BadRequest(new { message = "Passwords do not match." });
            }

            user.Pwd = _passwordHasher.HashPassword(user, request.NewPassword);
            user.IsFirstLogin = false;

            _context.Users.Update(user);
            await _context.SaveChangesAsync();

            _logger.LogInformation("Reset password success for email: {Email}", request.Email);
            return Ok(new { message = "Password reset successful." });
        }

        [AllowAnonymous]
        [HttpPost("SignUp")]
        public async Task<IActionResult> SignUp([FromBody] SignUpRequest request)
        {
            _logger.LogInformation("Sign up request received for email: {Email}", request.Email);

            if (request.Password != request.ConfirmPassword)
            {
                _logger.LogWarning("Sign up failed for email: {Email} - Passwords do not match", request.Email);
                return BadRequest(new { message = "Passwords do not match." });
            }

            var existingUser = await _context.Users.SingleOrDefaultAsync(u => u.Email == request.Email);
            if (existingUser != null)
            {
                _logger.LogWarning("Sign up failed for email: {Email} - Email already registered", request.Email);
                return BadRequest(new { message = "Email already registered." });
            }

            var user = new User
            {
                FirstName = request.FirstName,
                LastName = request.LastName,
                Email = request.Email,
                Role = "Guest",
                IsFirstLogin = false, // Assuming all newly registered users are required to reset password
                HasAccess = true,
                OTP = ""
            };

            user.Pwd = _passwordHasher.HashPassword(user, request.Password);

            _context.Users.Add(user);
            await _context.SaveChangesAsync();

            // Generate JWT token
            var tokenString = GenerateJwtToken(user);

            _logger.LogInformation("Sign up success for email: {Email}", request.Email);

            // Return token in the response
            return Ok(new { message = "Sign-up successful.", token = tokenString });
        }

        [Authorize]
        [HttpGet("ProtectedEndpoint")]
        [Authorize(Roles = "Admin")]
        public IActionResult ProtectedEndpoint()
        {
            var token = HttpContext.Request.Headers["Authorization"].ToString().Split(" ").Last();
            _logger.LogInformation("Token in ProtectedEndpoint: {Token}", token);

            _logger.LogInformation("Protected endpoint accessed by user ID: {UserId}", User.Identity.Name);
            return Ok(new { message = "You have access to this protected endpoint." });
        }

        public class LoginRequest
        {
            public string Email { get; set; } = string.Empty;
            public string Password { get; set; } = string.Empty;
        }

        public class ResetPasswordRequest
        {
            public string Email { get; set; } = string.Empty;
            public string NewPassword { get; set; } = string.Empty;
            public string ConfirmPassword { get; set; } = string.Empty;
        }

        public class SignUpRequest
        {
            public string FirstName { get; set; } = string.Empty;
            public string LastName { get; set; } = string.Empty;
            public string Email { get; set; } = string.Empty;
            public string Password { get; set; } = string.Empty;
            public string ConfirmPassword { get; set; } = string.Empty;
        }
    }
}
