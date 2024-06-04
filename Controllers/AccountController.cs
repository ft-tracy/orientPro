using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using System.Threading.Tasks;
using LoginApp.Data;
using LoginApp.Models;
using LoginApp.Services;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Authorization;
using Microsoft.Extensions.Logging;
using System;

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

        public AccountController(ApplicationDbContext context, IPasswordHasher<User> passwordHasher, UserService userService, ILogger<AccountController> logger)
        {
            _context = context;
            _passwordHasher = passwordHasher;
            _userService = userService;
            _logger = logger;
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

            if (user.IsFirstLogin)
            {
                if (user.Role == "Admin")
                {
                    if (request.Email != user.Email || request.Password != user.Pwd)
                    {
                        _logger.LogWarning("First login failed for admin email: {Email} - Invalid credentials", request.Email);
                        return Unauthorized(new { message = "Invalid credentials." });
                    }

                    _logger.LogInformation("First login success for admin email: {Email}", request.Email);
                    return Ok(new { message = "First login. Redirect to reset credentials.", redirectUrl = "/reset-password" });
                }
                else if (user.Role == "ContentManager" || user.Role == "Trainee")
                {
                    if (request.Password != user.OTP || request.Email != user.Email || user.OTPExpiration < DateTime.Now)
                    {
                        _logger.LogWarning("First login failed for {Role} email: {Email} - Invalid OTP or expired", user.Role, request.Email);
                        return Unauthorized(new { message = "Invalid OTP." });
                    }

                    _logger.LogInformation("First login success for {Role} email: {Email}", user.Role, request.Email);
                    return Ok(new { message = "First login. Redirect to reset credentials.", redirectUrl = "/reset-password" });
                }
            }
            else
            {
                if (_passwordHasher.VerifyHashedPassword(user, user.Pwd, request.Password) == PasswordVerificationResult.Failed)
                {
                    _logger.LogWarning("Subsequent login failed for email: {Email} - Invalid password", request.Email);
                    return Unauthorized(new { message = "Invalid credentials." });
                }
            }

            _logger.LogInformation("Login success for email: {Email}", request.Email);
            return Ok(new { message = "Login successful." });
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
                IsFirstLogin = false,
                HasAccess = true,
                OTP = ""
            };

            user.Pwd = _passwordHasher.HashPassword(user, request.Password);

            _context.Users.Add(user);
            await _context.SaveChangesAsync();

            _logger.LogInformation("Sign up success for email: {Email}", request.Email);
            return Ok(new { message = "Sign-up successful." });
        }

        [Authorize]
        [HttpGet("ProtectedEndpoint")]
        public IActionResult ProtectedEndpoint()
        {
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
