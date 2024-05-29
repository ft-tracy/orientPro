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
                        var kestrelConfig = context.Configuration.GetSection("Kestrel:Endpoints");

                        // Read HTTP endpoint configuration
                        var httpEndpoint = kestrelConfig.GetSection("Http:Url").Value;
                        if (!string.IsNullOrEmpty(httpEndpoint))
                        {
                            serverOptions.ListenAnyIP(new Uri(httpEndpoint).Port);
                        }

                        // Read HTTPS endpoint configuration
                        var httpsEndpoint = kestrelConfig.GetSection("Https:Url").Value;
                        var certConfig = kestrelConfig.GetSection("Https:Certificate");
                        var certPath = certConfig.GetValue<string>("Path");
                        var certPassword = certConfig.GetValue<string>("Password");

                        if (!string.IsNullOrEmpty(httpsEndpoint) && !string.IsNullOrEmpty(certPath) && !string.IsNullOrEmpty(certPassword))
                        {
                            serverOptions.ListenAnyIP(new Uri(httpsEndpoint).Port, listenOptions =>
                            {
                                listenOptions.UseHttps(certPath, certPassword);
                            });
                        }
                    });

                    // Check if Render environment variable exists
                    var renderPort = Environment.GetEnvironmentVariable("PORT");
                    if (!string.IsNullOrEmpty(renderPort) && int.TryParse(renderPort, out int port))
                    {
                        // Use the port specified by Render
                        webBuilder.UseUrls($"http://0.0.0.0:{port}");
                    }
                    else
                    {
                        // Use a default port if not running in Render
                        webBuilder.UseUrls("http://0.0.0.0:5000"); // Updated to use port 5000
                    }

                    webBuilder.UseStartup<Startup>();
                });
    }
}
