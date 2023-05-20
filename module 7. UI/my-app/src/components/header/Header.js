import React from 'react';
import {Container, Nav, Navbar, NavDropdown} from 'react-bootstrap';
import {LinkContainer} from 'react-router-bootstrap'
import '../../App.css';

function Header() {
  return (<header>
        <Navbar bg="dark" variant='dark' expand="lg" className=''>
          <Container>
            <LinkContainer to="/certificates" className='navbar-brand'>
              <Nav.Link>Gift Certificates</Nav.Link>
            </LinkContainer>
            <Navbar.Toggle aria-controls="basic-navbar-nav"/>
            <Navbar.Collapse id="basic-navbar-nav">
              <Nav className="me-auto">
                <LinkContainer to="/login">
                  <Nav.Link>Login</Nav.Link>
                </LinkContainer>
                <LinkContainer to="/add-certificate">
                  <Nav.Link>Add certificate</Nav.Link>
                </LinkContainer>
              </Nav>
              <Nav>
                <NavDropdown title="Dropdown" id="basic-nav-dropdown">
                  <NavDropdown.Item href="#action/3.1">Action</NavDropdown.Item>
                  <NavDropdown.Item href="#action/3.2">
                    Another action
                  </NavDropdown.Item>
                  <NavDropdown.Item
                      href="#action/3.3">Something</NavDropdown.Item>
                  <NavDropdown.Divider/>
                  <NavDropdown.Item href="#action/3.4">
                    Separated link
                  </NavDropdown.Item>
                </NavDropdown>
              </Nav>
            </Navbar.Collapse>
          </Container>
        </Navbar>
      </header>);
}

export default Header;