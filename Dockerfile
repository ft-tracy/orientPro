# Use the official .NET image
FROM mcr.microsoft.com/dotnet/aspnet:6.0 AS base
WORKDIR /app
EXPOSE 80

FROM mcr.microsoft.com/dotnet/sdk:6.0 AS build
WORKDIR /src
COPY ["LoginApp/LoginApp.csproj", "LoginApp/"]
RUN dotnet restore "LoginApp/LoginApp.csproj"
COPY . .
WORKDIR "/src/LoginApp"
RUN dotnet build "LoginApp.csproj" -c Release -o /app/build

FROM build AS publish
RUN dotnet publish "LoginApp.csproj" -c Release -o /app/publish

FROM base AS final
WORKDIR /app
COPY --from=publish /app/publish .
ENV ASPNETCORE_URLS=http://+:${PORT}
ENTRYPOINT ["dotnet", "LoginApp.dll"]
