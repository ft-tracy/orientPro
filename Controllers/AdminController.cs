using Microsoft.AspNetCore.Mvc;
using LoginApp.Models;
using LoginApp.Services;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Authorization;
using Microsoft.Extensions.Logging;

namespace LoginApp.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    [Authorize(Roles = "Admin")] // Ensure only admin users can access these endpoints
    public class AdminController : ControllerBase
    {
        private readonly UserService _userService;
        private readonly ILogger<AdminController> _logger; // Add this line

        public AdminController(UserService userService)
        {
            _userService = userService;
        }

        [HttpPost("AddUser")]
        public async Task<IActionResult> AddUser([FromBody] AddUserRequest request)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            var user = new User
            {
                FirstName = request.FirstName,
                LastName = request.LastName,
                Email = request.Email,
                Role = request.Role, // Assuming role is provided in the request
                IsFirstLogin = true // Set FirstLogin flag to true
            };

            try
            {
                await _userService.AddUserAsync(user, isCreatedByAdmin: true, isWebApp: true);
                return Ok(new { message = "User added successfully. OTP sent to user's email." });
            }
            catch (Exception ex)
            {
                _logger.LogError("An error occurred while adding the user: {Error}", ex.Message);
                return StatusCode(500, new { message = "An error occurred while adding the user." });
            }
        }

        public class AddUserRequest
        {
            public string FirstName { get; set; }
            public string LastName { get; set; }
            public string Email { get; set; }
            public string Role { get; set; } // Role like Admin, ContentManager, Trainee
        }
    }
}
