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
                            serverOptions.ListenAnyIP(int.Parse(GetPort()), listenOptions =>
                            {
                                listenOptions.UseHttps(certPath, certPassword);
                            });
                        }
                    });
                    webBuilder.UseStartup<Startup>()
                              .UseUrls("https://0.0.0.0:" + GetPort());
                });

        private static string GetPort()
        {
            var port = Environment.GetEnvironmentVariable("PORT");
            return string.IsNullOrEmpty(port) ? "5000" : port;
        }
    }
}
