import {Container, Row, Col, NavLink} from "react-bootstrap";

function Footer() {
  return (<footer className="page-footer font-small blue pt-4 fixed-bottom">
    <Container fluid className="text-center bg-dark text-light">
      <Row>
        <hr className="clearfix w-100 d-md-none pb-0"/>
        <Col className="footer-copyright text-center py-3">
          <NavLink path="#MJC">© 2023 Copyright MJC School.</NavLink>
        </Col>
      </Row>
    </Container>
  </footer>)
}

export default Footer;