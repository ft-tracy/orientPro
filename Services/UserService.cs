using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Mail;
using System.Threading.Tasks;
using OnboardPro.Models;
using OnboardPro.Data;

namespace OnboardPro.Services
{
    //Service for adding a user, generating an OTP and sending the email
    public class UserService
    {
        private readonly UserContext _context;

        public UserService(UserContext context)
        {
            _context = context;
        }

        //Calls functions that do all three things
        public async Task AddUserAsync(User user)
        {
            user.OTP = GenerateOtp();
            _context.Users.Add(user);
            await _context.SaveChangesAsync();
            SendOtpEmail(user);
        }

        //Generates OTP
        private string GenerateOtp(int length = 6)
        {
            Random random = new Random();
            const string chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ123456789";
            return new string(Enumerable.Repeat(chars, length).Select(s => s[random.Next(s.Length)]).ToArray());
        }

        //Drafts and send email with first time login details
        private void SendOtpEmail(User user)
        {
            var fromAddress = new MailAddress("onboardproinfo@gmail.com", "OnboardPro");
            var toAddress = new MailAddress(user.Email, user.Username);
            const string fromPassword = "OnboardPro2024";
            const string subject = "Your OTP Code";

            string resetLink;
            if (user.Role == "Admin" || user.Role == "Supervisor")
            {
                resetLink = $"https://yourdomain.com/admin/reset-password?otp={user.OTP}";
            }
            else if (user.Role == "Trainee" || user.Role == "Guest")
            {
                resetLink = $"https://yourdomain.com/trainee/reset-password?otp={user.OTP}";
            }
            else
            {
                throw new Exception("Invalid user role");
            }

            var emailBody = $"Hello {user.FirstName},\n\n" + $"Please use the following OTP to set your password and username: {user.OTP}\n\n" + $"Your default username is your email: {user.Email}\n\n" + $"Click here to reset your password and set your username: {resetLink}\n\n" + $"This OTP is valid for one hour.";

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

        //Seervice to find a user by the OTP input
        public User GetUserByOtp(string otp)
        {
            return _context.Users.FirstOrDefault(u => u.OTP == otp);
        }

        public User GetUserByUsername(string username)
        {
            return _context.Users.FirstOrDefault(u => u.Username == username);
        }

        public User GetUserByEmail(string email)
        {
            return _context.Users.FirstOrDefault(u => u.Email == email);
        }

    }
}