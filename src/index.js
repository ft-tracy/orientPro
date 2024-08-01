import React from "react";
import ReactDOM from "react-dom/client";
import "bootstrap/dist/css/bootstrap.css";
import "./styles/index.css";
import App from "./App";
import reportWebVitals from "./reportWebVitals";
import { AuthProvider } from "./contexts/AuthContext";
import TimeAgo from "javascript-time-ago";
import en from "javascript-time-ago/locale/en.json";
import ru from "javascript-time-ago/locale/en.json";

TimeAgo.addDefaultLocale(en);
TimeAgo.addLocale(ru);

const root = ReactDOM.createRoot(document.getElementById("root"));
root.render(
  //<React.StrictMode>
  <AuthProvider>
    <App />
  </AuthProvider>
  //</React.StrictMode>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
