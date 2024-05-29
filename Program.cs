using Microsoft.AspNetCore.Hosting;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.Hosting;
using System;
using System.Linq;
using System.Net;
using System.Net.Sockets;

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

                        var port = GetAvailablePort(5000, 5100); // Check ports between 5000 and 5100

                        if (!string.IsNullOrEmpty(certPath) && !string.IsNullOrEmpty(certPassword))
                        {
                            serverOptions.ListenAnyIP(port, listenOptions =>
                            {
                                listenOptions.UseHttps(certPath, certPassword);
                            });
                        }
                    });
                    webBuilder.UseStartup<Startup>()
                              .UseUrls("https://0.0.0.0:" + Environment.GetEnvironmentVariable("PORT") ?? "5000");
                });

        private static int GetAvailablePort(int minPort, int maxPort)
        {
            var usedPorts = IPGlobalProperties.GetIPGlobalProperties()
                .GetActiveTcpListeners()
                .Select(p => p.Port)
                .ToArray();

            for (int port = minPort; port <= maxPort; port++)
            {
                if (!usedPorts.Contains(port))
                {
                    return port;
                }
            }
            throw new Exception("No available ports in the specified range.");
        }
    }
}
