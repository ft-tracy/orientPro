using Microsoft.AspNetCore.Mvc;
using LoginApp.Models;
using LoginApp.Services;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Authorization;

namespace LoginApp.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
   // [Authorize(Roles = "Admin")] // Ensure only admin users can access these endpoints
    public class AdminController : ControllerBase
    {
        private readonly UserService _userService;

        public AdminController(UserService userService)
        {
            _userService = userService;
        }

        [HttpPost("AddUser")]
        public async Task<IActionResult> AddUser([FromBody] AddUserRequest request)
        {
            // Validate request
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            // Create user object
            var user = new User
            {
                FirstName = request.FirstName,
                LastName = request.LastName,
                Email = request.Email,
                Role = request.Role // Assuming role is provided in the request
            };

            try
            {
                // Add user using UserService
                await _userService.AddUserAsync(user, isCreatedByAdmin: true, isWebApp: true);
                return Ok(new { message = "User added successfully. OTP sent to user's email." });
            }
            catch (Exception ex)
            {
                // Handle any errors
                return StatusCode(500, new { message = "An error occurred while adding the user." });
            }
        }

        // Model for adding a new user
        public class AddUserRequest
        {
            public string FirstName { get; set; } = string.Empty;
            public string LastName { get; set; } = string.Empty;
            public string Email { get; set; } = string.Empty;
            public string Role { get; set; } = string.Empty; // Add property for role
        }
    }
}
