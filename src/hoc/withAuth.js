import React from "react";
import { AuthContext } from "../contexts/AuthContext";

export const withAuth = (Component) => {
  return function AuthComponent(props) {
    return (
      <AuthContext.Consumer>
        {(authContext) => <Component {...props} authContext={authContext} />}
      </AuthContext.Consumer>
    );
  };
};
