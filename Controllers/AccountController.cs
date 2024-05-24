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
            var user = await _context.Users.SingleOrDefaultAsync(u => u.Email == request.Email);

            if (user == null)
            {
                return Unauthorized(new { message = "Invalid credentials." });
            }

            // Check if it's the user's first login
            if (user.IsFirstLogin)
            {
                // For admin login
                if (user.Role == "Admin")
                {
                    // Verify that the provided email matches the email in the database
                    if (request.Email != user.Email)
                    {
                        return Unauthorized(new { message = "Invalid credentials." });
                    }

                    // Check if the provided password matches the password in the database (initially set for the admin)
                    if (request.Password != user.Pwd)
                    {
                        return Unauthorized(new { message = "Invalid credentials." });
                    }

                    // If credentials are correct, redirect to reset password page
                    return Ok(new { message = "First login. Redirect to reset credentials.", redirectUrl = "/reset-password" });
                }
                else // For guest login
                {
                    // Verify the OTP entered by the user
                    if (request.Password != user.OTP)
                    {
                        return Unauthorized(new { message = "Invalid OTP." });
                    }

                    // Verify that the email provided during login matches the email to which the OTP was sent
                    if (request.Email != user.Email)
                    {
                        return Unauthorized(new { message = "Invalid email." });
                    }

                    // If OTP is correct and email matches, redirect to reset password page
                    return Ok(new { message = "First login. Redirect to reset credentials.", redirectUrl = "/reset-password" });
                }
            }
            else // For subsequent logins
            {
                // For admin login, after resetting password
                if (user.Role == "Admin")
                {
                    // Verify that the provided email matches the email in the database
                    if (request.Email != user.Email)
                    {
                        return Unauthorized(new { message = "Invalid credentials." });
                    }

                    // Check if the provided password matches the password in the database (initially set for the admin)
                    if (_passwordHasher.VerifyHashedPassword(user, user.Pwd, request.Password) == PasswordVerificationResult.Failed)
                    {
                        return Unauthorized(new { message = "Invalid credentials." });
                    }
                }
                else // For guest login
                {
                    // Verify hashed password
                    if (_passwordHasher.VerifyHashedPassword(user, user.Pwd, request.Password) == PasswordVerificationResult.Failed)
                    {
                        return Unauthorized(new { message = "Invalid credentials." });
                    }
                }
            }

            return Ok(new { message = "Login successful." });
        }




        [HttpPost("ResetPassword")]
        public async Task<IActionResult> ResetPassword([FromBody] ResetPasswordRequest request)
        {
            var user = await _context.Users.SingleOrDefaultAsync(u => u.Email == request.Email);
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

        public class LoginRequest
        {
            public string Email { get; set; }
            public string Password { get; set; }
        }

        public class ResetPasswordRequest
        {
            public string Email { get; set; }  // For verifying the user
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
}

