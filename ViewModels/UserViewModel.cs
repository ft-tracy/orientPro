using OnboardPro.Models;

namespace OnboardPro.ViewModels
{
    public class UserViewModel
    {
        public string Username { get; set; }
        public string Email { get; set; }
        public string FirstName { get; set; }
        public string LastName { get; set; }
        public string Role { get; set; }

        public UserViewModel() { }

        public UserViewModel(User user)
        {
            Username = user.Username;
            Email = user.Email;
            FirstName = user.FirstName;
            Role = user.Role;
        }

        public User ToModel()
        {
            return new User
            {
                Username = Username,
                Email = Email,
                FirstName = FirstName,
                LastName = LastName,
                Role = Role
            };
        }
    }
}