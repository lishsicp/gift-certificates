import { useSelector } from "react-redux";
import { AUTHORIZE_ENDPOINT } from "../../authorizeEndpoint";
import { selectCurrentAccessToken } from "./authTokenSlice";
import React, { useEffect, useState } from "react";

const RequireAuth = ({ child }) => {
  const accessToken =
    useSelector(selectCurrentAccessToken) ||
    localStorage.getItem("accessToken");
  const [isActivated, setActivated] = useState(false);

  useEffect(() => {
    if (isActivated && !accessToken) {
      window.location.href = `${AUTHORIZE_ENDPOINT}`;
    }
  }, [accessToken, isActivated]);

  const handleChildClick = () => {
    setActivated(true);
  };

  const childWithProps = React.cloneElement(child, {
    onClick: handleChildClick,
  });

  return <>{accessToken ? childWithProps : null}</>;
};

export default RequireAuth;
