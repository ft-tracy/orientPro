namespace LoginApp.Models
{
    public class User
    {
        public int UniqueID { get; set; }
        public string Name { get; set; }
        public string Username { get; set; }
        public string Pwd { get; set; }
        public string Email { get; set; }
        public string Role { get; set; }
        public DateTime DateJoined { get; set; }
        public bool HasAccess { get; set; }
        public bool IsFirstLogin { get; set; }
    }
}
