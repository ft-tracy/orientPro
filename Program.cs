using Microsoft.AspNetCore.Hosting;
using Microsoft.Extensions.Hosting;

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
                        var url = context.Configuration["Kestrel:Endpoints:Https:Url"];

                        if (!string.IsNullOrEmpty(url))
                        {
                            serverOptions.ListenAnyIP(443, listenOptions =>
                            {
                                listenOptions.UseHttps();
                            });
                        }
                    });
                    webBuilder.UseStartup<Startup>();
                });
    }
}
