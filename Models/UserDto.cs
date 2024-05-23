using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace OnboardPro.Models
{
    public class UserDto
    {
        public required string Username { get; set; }
        public required string Password { get; set; }
    }
}