using Microsoft.AspNetCore.Hosting;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.Hosting;
using System;
using System.Security.Cryptography.X509Certificates;

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
                            serverOptions.ConfigureHttpsDefaults(listenOptions =>
                            {
                                listenOptions.ServerCertificate = new X509Certificate2(certPath, certPassword);
                            });
                        }
                    });

                    // Use the port specified by Render, otherwise default ports will be used
                    var renderPort = Environment.GetEnvironmentVariable("PORT");
                    if (!string.IsNullOrEmpty(renderPort))
                    {
                        webBuilder.UseUrls($"http://0.0.0.0:{renderPort}");
                    }

                    webBuilder.UseStartup<Startup>();
                });
    }
}
