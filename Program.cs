using System;
using Microsoft.AspNetCore.Hosting;
using Microsoft.Extensions.Hosting;

namespace LoginApp
{
    public class Program
    {
        public static void Main(string[] args)
        {
            // Read the port number from the PORT environment variable
            string port = Environment.GetEnvironmentVariable("PORT") ?? "5000";

            CreateHostBuilder(args, port).Build().Run();
        }

        public static IHostBuilder CreateHostBuilder(string[] args, string port) =>
            Host.CreateDefaultBuilder(args)
                .ConfigureWebHostDefaults(webBuilder =>
                {
                    webBuilder.UseStartup<Startup>()
                              .UseUrls($"http://0.0.0.0:{port}");
                });
    }
}
