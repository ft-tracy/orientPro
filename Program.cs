using Microsoft.AspNetCore.Hosting;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.Hosting;
using System;

namespace LoginApp
{
    public class Program
    {
        public static void Main(string[] args)
        {
            CreateHostBuilder(args).Build().Run();
        }

        public static IHostBuilder CreateHostBuilder(string[] args) =>
            Host.CreateDefaultBuilder(args)
                .ConfigureWebHostDefaults(webBuilder =>
                {
                    webBuilder.ConfigureKestrel((context, serverOptions) =>
                    {
                        var certConfig = context.Configuration.GetSection("Kestrel:Endpoints:Https:Certificate");
                        var certPath = certConfig.GetValue<string>("Path");
                        var certPassword = certConfig.GetValue<string>("Password");

                        if (!string.IsNullOrEmpty(certPath) && !string.IsNullOrEmpty(certPassword))
                        {
                            serverOptions.ListenAnyIP(0, listenOptions =>
                            {
                                listenOptions.UseHttps(certPath, certPassword);
                            });
                        }
                    });

                    // Check if Render environment variable exists
                    var renderPort = Environment.GetEnvironmentVariable("PORT");
                    if (!string.IsNullOrEmpty(renderPort))
                    {
                        // Use the port specified by Render
                        webBuilder.UseUrls($"https://0.0.0.0:{renderPort}");
                    }
                    else
                    {
                        // Use a default port if not running in Render
                        webBuilder.UseUrls("https://0.0.0.0:5000");
                    }

                    webBuilder.UseStartup<Startup>();
                });
    }
}
