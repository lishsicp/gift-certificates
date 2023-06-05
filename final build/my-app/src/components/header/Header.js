import React, { useCallback } from "react";
import { Container, Nav, Navbar } from "react-bootstrap";
import { useDispatch, useSelector } from "react-redux";
import { LinkContainer } from "react-router-bootstrap";
import { useNavigate } from "react-router-dom";
import { AUTHORIZE_ENDPOINT, BASE_URL } from "../../authorizeEndpoint";
import {
  resetAuthState,
  selectCurrentAccessToken,
  selectCurrentUser,
} from "../../features/auth/authTokenSlice";
import RequireAuth from "../../features/auth/RequireAuth";
import AddCertificateModal from "../certificates/modals/addCertificateModal";
import { BoxArrowRight } from "react-bootstrap-icons";

function Header() {
  let navigate = useNavigate();
  let dispatch = useDispatch();
  const handleLogout = useCallback(() => {
    dispatch(resetAuthState());
    navigate("/certificates");
  }, [dispatch, navigate]);
  const accessToken =
    useSelector(selectCurrentAccessToken) ||
    localStorage.getItem("accessToken");
  const currectUser =
    useSelector(selectCurrentUser) || localStorage.getItem("email");

  return (
    <header>
      <Navbar bg="dark" variant="dark" expand="lg" className="">
        <Container>
          <LinkContainer to="/certificates" className="navbar-brand">
            <Nav.Link>Gift Certificates</Nav.Link>
          </LinkContainer>
          <RequireAuth child={<AddCertificateModal />} />
          <Navbar.Toggle aria-controls="basic-navbar-nav" />
          <Navbar.Collapse id="basic-navbar-nav">
            <Nav className="me-auto">
              {!accessToken ? (
                <Nav.Link href={AUTHORIZE_ENDPOINT}>Login</Nav.Link>
              ) : null}
            </Nav>
            <Nav>{accessToken ? <Nav.Link>{currectUser}</Nav.Link> : null}</Nav>
            {accessToken ? (
              <Nav>
                <Nav.Link
                  href={`${BASE_URL}`}
                  onClick={handleLogout}
                  target="_blank"
                >
                  Logout
                  <BoxArrowRight size="20" className="ms-2" />
                </Nav.Link>
              </Nav>
            ) : null}
          </Navbar.Collapse>
        </Container>
      </Navbar>
    </header>
  );
}

export default Header;
