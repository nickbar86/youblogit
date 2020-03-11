import React from "react";
import { Route, Redirect } from "react-router-dom";

const PrivateRoute = ({ component: Component, isAllowed, ...rest }) => {
  return (
    <Route
      {...rest}
      render={props => {
        return isAllowed ? (
          <Component {...props} />
        ) : (
          <Redirect
            isAllowed={isAllowed}
            to={{
              pathname: "/forbidden"
            }}
          />
        );
      }}
    />
  );
};

export default PrivateRoute;
