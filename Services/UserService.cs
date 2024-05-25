using System;
using System.Linq;
using System.Net;
using System.Net.Mail;
using System.Threading.Tasks;
using LoginApp.Data;
using LoginApp.Models;
using Microsoft.EntityFrameworkCore;
using static System.Net.WebRequestMethods;

namespace LoginApp.Services
{
    public class UserService
    {
        private readonly ApplicationDbContext _context;

        public UserService(ApplicationDbContext context)
        {
            _context = context;
        }

        // Method to add a user, generate an OTP, and send an email
        public async Task AddUserAsync(User user, bool isCreatedByAdmin = false, bool isWebApp = true)
        {
            if (user == null || string.IsNullOrWhiteSpace(user.Email))
            {
                throw new ArgumentException("Invalid user or email.");
            }

            user.OTP = GenerateOtp();
            user.OTPExpiration = DateTime.UtcNow.AddHours(1);  // Ensure you have a property for OTP expiration
            _context.Users.Add(user);
            await _context.SaveChangesAsync();
            SendOtpEmail(user, isCreatedByAdmin, isWebApp);
        }

        // Method to generate an OTP
        private string GenerateOtp(int length = 6)
        {
            var random = new Random();
            const string chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ123456789";
            return new string(Enumerable.Repeat(chars, length).Select(s => s[random.Next(s.Length)]).ToArray());
        }

        // Method to send an OTP email
        private void SendOtpEmail(User user, bool isCreatedByAdmin, bool isWebApp)
        {
            var fromAddress = new MailAddress("orientpro858@gmail.com", "OrientPro");
            var toAddress = new MailAddress(user.Email);
            const string fromPassword = "gfqdnjgrffegbpsx";
            const string subject = "Your OTP Code";


            string resetLink;

            if (user.Role == "Admin" || user.Role == "Supervisor")
            {
                resetLink = $"https://yourdomain.com/admin/reset-password?otp={user.OTP}";
            }
            else if (user.Role == "Trainee" || user.Role == "Guest")
            {
                if (isCreatedByAdmin)
                {
                    resetLink = $"https://yourdomain.com/trainee/reset-password?otp={user.OTP}";
                }
                else
                {
                    if (isWebApp)
                    {
                        resetLink = $"https://yourdomain.com/trainee/reset-password?otp={user.OTP}";
                    }
                    else
                    {
                        resetLink = $"mobileapp://reset-password?otp={user.OTP}";
                    }
                }
            }
            else
            {
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
        }

        // Method to find a user by the OTP
        public User GetUserByOtp(string otp)
        {
            return _context.Users.FirstOrDefault(u => u.OTP == otp && u.OTPExpiration > DateTime.UtcNow);
        }

        // Method to find a user by their email
        public User GetUserByEmail(string email)
        {
            return _context.Users.FirstOrDefault(u => u.Email == email);
        }
    }
}
