using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using OnboardPro.Services;
using OnboardPro.ViewModels;

namespace OnboardPro.Controllers
{
    //API controller to handle user addition
    [ApiController]
    [Route("api/[controller]")]
    public class UsersController : ControllerBase
    {
        private readonly UserService _userService;

        public UsersController(UserService userService)
        {
            _userService = userService;
        }

        [HttpPost, Authorize] //Can add other constraints such as roles i.e: Authorize(Role= "Admin")
        public async Task<IActionResult> Create(UserViewModel userViewModel)
        {
            var user = userViewModel.ToModel();
            await _userService.AddUserAsync(user);
            return Ok();
        }
    }
}