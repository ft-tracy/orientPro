#nullable enable

using System;
using System.ComponentModel.DataAnnotations;

namespace LoginApp.Models
{
    public class User
    {
        [Key]
        public int UniqueID { get; set; }

        public string FirstName { get; set; } = string.Empty;

        public string Pwd { get; set; } = string.Empty;

        public string Email { get; set; } = string.Empty;

        public string Role { get; set; } = string.Empty;

        public DateTime DateJoined { get; set; }

        public bool HasAccess { get; set; }

        public bool IsFirstLogin { get; set; }

        public string? OTP { get; set; }  // Nullable type since it might not always be set

        public DateTime? OTPExpiration { get; set; }  // Nullable type since it might not always be set

        public string LastName { get; set; } = string.Empty;
    }
}
