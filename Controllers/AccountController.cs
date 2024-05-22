using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using System.Threading.Tasks;
using LoginApp.Data;
using LoginApp.Models;
using Microsoft.AspNetCore.Identity;

namespace LoginApp.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class AccountController : ControllerBase
    {
        private readonly ApplicationDbContext _context;
        private readonly IPasswordHasher<User> _passwordHasher;

        public AccountController(ApplicationDbContext context, IPasswordHasher<User> passwordHasher)
        {
            _context = context;
            _passwordHasher = passwordHasher;
        }

        [HttpPost("Login")]
        public async Task<IActionResult> Login([FromBody] LoginRequest request)
        {
            var user = await _context.Users.SingleOrDefaultAsync(u => u.Username == request.Username);
            if (user == null || _passwordHasher.VerifyHashedPassword(user, user.Pwd, request.Password) == PasswordVerificationResult.Failed)
            {
                return Unauthorized(new { message = "Invalid credentials." });
            }

            if (user.Role == "Admin" && user.IsFirstLogin)
            {
                // Redirect to password reset page
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

            user.Pwd = _passwordHasher.HashPassword(user, request.NewPassword);
            user.Name = request.Name;
            user.Email = request.Email;
            user.Role = request.Role;
            user.IsFirstLogin = false;

            _context.Users.Update(user);
            await _context.SaveChangesAsync();

            return Ok(new { message = "Password reset successful." });
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
        public string Name { get; set; }
        public string Email { get; set; }
        public string Role { get; set; }
    }
}
 