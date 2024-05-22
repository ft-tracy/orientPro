using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using OnboardPro.Services;
using OnboardPro.ViewModels;

namespace OnboardPro.Controllers
{
    //API controlle to handle user addition
    [ApiController]
    [Route("api/[controller]")]
    public class UsersController : ControllerBase
    {
        private readonly UserService _userService;

        public UsersController(UserService userService)
        {
            _userService = userService;
        }

        [HttpPost]
        public async Task<IActionResult> Create(UserViewModel userViewModel)
        {
            var user = userViewModel.ToModel();
            await _userService.AddUserAsync(user);
            return Ok();
        }
    }
}