// UserController.cs

using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using YourNamespace.Models;

namespace YourNamespace.Controllers
{
    public class UserController : ApiController
    {
        [HttpPost]
        public HttpResponseMessage Login(User user)
        {
            // Validate user credentials (this is a simple example, do proper validation in a real app)
            if (user != null && !string.IsNullOrEmpty(user.Username) && !string.IsNullOrEmpty(user.Password))
            {
                // Simulate user authentication (replace with your actual authentication logic)
                if (user.Username == "admin" && user.Password == "password")
                {
                    // Successful login
                    return Request.CreateResponse(HttpStatusCode.OK, new { message = "Login successful" });
                }
                else
                {
                    // Unauthorized
                    return Request.CreateResponse(HttpStatusCode.Unauthorized, new { message = "Invalid credentials" });
                }
            }
            else
            {
                // Bad request
                return Request.CreateResponse(HttpStatusCode.BadRequest, new { message = "Invalid request" });
            }
        }
    }
}
