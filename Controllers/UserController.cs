using Microsoft.AspNetCore.Mvc;
using LoginApp.Services;
using Microsoft.AspNetCore.Authorization;
using System.Security.Claims;
using Microsoft.Extensions.Logging;

namespace LoginApp.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class UserController : ControllerBase
    {
        private readonly UserService _userService;
        private readonly ILogger<UserController> _logger;

        public UserController(UserService userService, ILogger<UserController> logger)
        {
            _userService = userService;
            _logger = logger;
        }

        [HttpGet("GetUser")] // Updated route
        [Authorize]
        public async Task<IActionResult> GetUser()
        {
            var userId = User.FindFirstValue(ClaimTypes.NameIdentifier);
            _logger.LogInformation("GetUser called by userId: {UserId}", userId);

            var user = await _userService.GetUserByIdAsync(userId);

            if (user == null)
            {
                _logger.LogWarning("User not found for userId: {UserId}", userId);
                return NotFound();
            }

            _logger.LogInformation("User details retrieved successfully for userId: {UserId}", userId);
            return Ok(user);
        }
    }
}
