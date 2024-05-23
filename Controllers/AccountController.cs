using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using System.Threading.Tasks;
using LoginApp.Data;
using LoginApp.Models;
using LoginApp.Services;
using Microsoft.AspNetCore.Identity;

namespace LoginApp.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class AccountController : ControllerBase
    {
        private readonly ApplicationDbContext _context;
        private readonly IPasswordHasher<User> _passwordHasher;
        private readonly UserService _userService;

        public AccountController(ApplicationDbContext context, IPasswordHasher<User> passwordHasher, UserService userService)
        {
            _context = context;
            _passwordHasher = passwordHasher;
            _userService = userService;
        }

        [HttpPost("Login")]
        public async Task<IActionResult> Login([FromBody] LoginRequest request)
        {
            var user = await _context.Users.SingleOrDefaultAsync(u => u.Username == request.Username);
            if (user == null || (user.IsFirstLogin && user.Pwd != request.Password))
            {
                return Unauthorized(new { message = "Invalid credentials." });
            }

            if (!user.IsFirstLogin && _passwordHasher.VerifyHashedPassword(user, user.Pwd, request.Password) == PasswordVerificationResult.Failed)
            {
                return Unauthorized(new { message = "Invalid credentials." });
            }

            if (user.Role == "Admin" && user.IsFirstLogin)
            {
                return Ok(new { message = "First login. Redirect to reset credentials.", redirectUrl = "/reset-password" });
            }

            return Ok(new { message = "Login successful." });
        }

        [HttpPost("ResetPassword")]
        public async Task<IActionResult> ResetPassword([FromBody] ResetPasswordRequest request)
        {
            var user = await _context.Users.SingleOrDefaultAsync(u => u.Username == request.Username);
            if (user == null)
            {
                return NotFound(new { message = "User not found." });
            }

            if (request.NewPassword != request.ConfirmPassword)
            {
                return BadRequest(new { message = "Passwords do not match." });
            }

            user.Pwd = _passwordHasher.HashPassword(user, request.NewPassword);
            user.IsFirstLogin = false;

            _context.Users.Update(user);
            await _context.SaveChangesAsync();

            return Ok(new { message = "Password reset successful." });
        }

        [HttpPost("SignUp")]
        public async Task<IActionResult> SignUp([FromBody] SignUpRequest request)
        {
            var existingUser = await _context.Users.SingleOrDefaultAsync(u => u.Email == request.Email);
            if (existingUser != null)
            {
                return BadRequest(new { message = "Email already registered." });
            }

            var user = new User
            {
                FirstName = request.FirstName,
                LastName = request.LastName,
                Email = request.Email,
                Role = "Guest",
                IsFirstLogin = true,
                HasAccess = true
            };

            await _userService.AddUserAsync(user);

            return Ok(new { message = "Sign-up successful. OTP has been sent to your email." });
        }

    }

    public class LoginRequest
    {
        public string Username { get; set; }
        public string Password { get; set; }
    }

    public class ResetPasswordRequest
    {
        public string Username { get; set; }
        public string NewPassword { get; set; }
        public string ConfirmPassword { get; set; }
       
        
    }

    public class SignUpRequest
    {
        public string FirstName { get; set; }
        public string LastName { get; set; }
        public string Email { get; set; }
    }

}
