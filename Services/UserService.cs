using System;
using System.Linq;
using System.Net;
using System.Net.Mail;
using System.Threading.Tasks;
using LoginApp.Data;
using LoginApp.Models;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Logging;

namespace LoginApp.Services
{
    public class UserService
    {
        private readonly ApplicationDbContext _context;
        private readonly ILogger<UserService> _logger;

        public UserService(ApplicationDbContext context, ILogger<UserService> logger)
        {
            _context = context;
            _logger = logger;
        }

        public async Task AddUserAsync(User user, bool isCreatedByAdmin = false, bool isWebApp = true)
        {
            if (user == null || string.IsNullOrWhiteSpace(user.Email))
            {
                _logger.LogWarning("Invalid user or email during AddUserAsync");
                throw new ArgumentException("Invalid user or email.");
            }

            if (user.Role == "ContentManager" || user.Role == "Trainee")
            {
                user.OTP = GenerateOtp();
                user.OTPExpiration = DateTime.UtcNow.AddHours(5);
                user.IsFirstLogin = true;
            }
            else
            {
                user.OTP = null;
            }

            _context.Users.Add(user);
            await _context.SaveChangesAsync();
            _logger.LogInformation("User added successfully: {Email}", user.Email);
            SendOtpEmail(user, isCreatedByAdmin, isWebApp);
        }

        public User GetUserById(string userId)
        {
            // Parse the userId string to an integer
            if (!int.TryParse(userId, out int parsedUserId))
            {
                _logger.LogError("Failed to parse userId to integer.");
                return null; // Return null if parsing fails
            }

            _logger.LogInformation("Retrieving user by ID: {UserId}", parsedUserId);

            // Compare the parsed integer userId with UniqueID
            return _context.Users.FirstOrDefault(u => u.UniqueID == parsedUserId);
        }

        private string GenerateOtp(int length = 6)
        {
            var random = new Random();
            const string chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ123456789";
            return new string(Enumerable.Repeat(chars, length).Select(s => s[random.Next(s.Length)]).ToArray());
        }

        private void SendOtpEmail(User user, bool isCreatedByAdmin, bool isWebApp)
        {
            if (user.OTP == null)
            {
                _logger.LogInformation("No OTP to send for email: {Email}", user.Email);
                return;
            }

            var fromAddress = new MailAddress("orientpro858@gmail.com", "OrientPro");
            var toAddress = new MailAddress(user.Email);
            const string fromPassword = "gfqdnjgrffegbpsx";
            const string subject = "Your OTP Code";

            string resetLink;

            if (user.Role == "Admin" || user.Role == "ContentManager" || user.Role == "Trainee")
            {
                resetLink = $"https://yourdomain.com/reset-password?otp={user.OTP}";
            }
            else
            {
                _logger.LogError("Invalid user role: {Role} for email: {Email}", user.Role, user.Email);
                throw new Exception("Invalid user role");
            }

            var emailBody = $"Hello {user.FirstName} {user.LastName},\n\n" +
                            $"Please use the following OTP to set your password: {user.OTP}\n\n" +
                            $"Your default username is your email: {user.Email}\n\n" +
                            $"Click here to reset your password and set your username: https://orientpro.onrender.com/\n\n" +
                            $"This OTP is valid for one hour.";

            var smtp = new SmtpClient
            {
                Host = "smtp.gmail.com",
                Port = 587,
                EnableSsl = true,
                DeliveryMethod = SmtpDeliveryMethod.Network,
                UseDefaultCredentials = false,
                Credentials = new NetworkCredential(fromAddress.Address, fromPassword)
            };

            using (var message = new MailMessage(fromAddress, toAddress)
            {
                Subject = subject,
                Body = emailBody
            })
            {
                smtp.Send(message);
            }

            _logger.LogInformation("OTP email sent to: {Email}", user.Email);
        }

        public User GetUserByOtp(string otp)
        {
            _logger.LogInformation("Retrieving user by OTP: {Otp}", otp);
            return _context.Users.FirstOrDefault(u => u.OTP == otp && u.OTPExpiration > DateTime.UtcNow);
        }
    }
}
