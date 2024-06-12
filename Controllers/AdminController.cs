using Microsoft.AspNetCore.Mvc;
using LoginApp.Models;
using LoginApp.Services;
using System.Collections.Generic;
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
        private readonly ILogger<AdminController> _logger;

        public AdminController(UserService userService, ILogger<AdminController> logger)
        {
            _userService = userService;
            _logger = logger;
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
                Role = request.Role,
                IsFirstLogin = true // Set FirstLogin flag to true
            };

            try
            {
                await _userService.AddUserAsync(user);
                return Ok(new { message = "User added successfully. OTP sent to user's email." });
            }
            catch (Exception ex)
            {
                _logger.LogError("An error occurred while adding the user: {Error}", ex.Message);
                return StatusCode(500, new { message = "An error occurred while adding the user." });
            }
        }

        [HttpGet("GetAllUsers")]
        public async Task<IActionResult> GetAllUsers()
        {
            try
            {
                var users = await _userService.GetAllUsersAsync();
                // Create a simplified list of user objects containing only necessary fields
                var simplifiedUsers = users.Select(u => new
                {
                    u.UniqueID,
                    u.FirstName,
                    u.LastName,
                    u.Email,
                    u.Role
                });
                return Ok(simplifiedUsers);
            }
            catch (Exception ex)
            {
                _logger.LogError("An error occurred while fetching users: {Error}", ex.Message);
                return StatusCode(500, new { message = "An error occurred while fetching users." });
            }
        }


        [HttpPut("UpdateUser/{id}")]
        public async Task<IActionResult> UpdateUser(string id, [FromBody] UpdateUserRequest request)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            try
            {
                var user = await _userService.GetUserByIdAsync(id);
                if (user == null)
                {
                    return NotFound(new { message = "User not found." });
                }

                user.Email = request.Email;
                user.Role = request.Role;

                await _userService.UpdateUserAsync(user);
                return Ok(new { message = "User updated successfully." });
            }
            catch (Exception ex)
            {
                _logger.LogError("An error occurred while updating the user: {Error}", ex.Message);
                return StatusCode(500, new { message = "An error occurred while updating the user." });
            }
        }

        [HttpDelete("DeleteUser/{id}")]
        public async Task<IActionResult> DeleteUser(string id)
        {
            try
            {
                var user = await _userService.GetUserByIdAsync(id);
                if (user == null)
                {
                    return NotFound(new { message = "User not found." });
                }

                await _userService.DeleteUserAsync(user);
                return Ok(new { message = "User deleted successfully." });
            }
            catch (Exception ex)
            {
                _logger.LogError("An error occurred while deleting the user: {Error}", ex.Message);
                return StatusCode(500, new { message = "An error occurred while deleting the user." });
            }
        }

        public class AddUserRequest
        {
            public string FirstName { get; set; }
            public string LastName { get; set; }
            public string Email { get; set; }
            public string Role { get; set; } // Role like Admin, ContentManager, Trainee
        }

        public class UpdateUserRequest
        {
            public string Email { get; set; }
            public string Role { get; set; }
        }
    }
}
