using Microsoft.EntityFrameworkCore;
using OnboardPro.Models;

namespace OnboardPro.Data
{
    public class UserContext : DbContext
    {
        public UserContext(DbContextOptions<UserContext> options) : base(options) { }

        public DbSet<User> Users { get; set; }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            base.OnModelCreating(modelBuilder);

            //Configuring default values and nullable properties
            modelBuilder.Entity<User>(entity =>
            {
                entity.Property(e => e.Username).IsRequired();
                entity.Property(e => e.Email).IsRequired();
                entity.Property(e => e.FirstName).IsRequired();
                entity.Property(e => e.LastName).IsRequired();
                entity.Property(e => e.Role).IsRequired();
                entity.Property(e => e.OTP).HasDefaultValue(string.Empty);
            });

        }
    }
}