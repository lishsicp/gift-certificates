import React from 'react';
import {Container, Form, Row, Col, Card, Button} from 'react-bootstrap'

function Login() {
  return (<Container>
    <Row
        className="vh-100 d-flex justify-content-center align-items-center">
      <Col md={6} lg={4} xs={8}>
        <Card className='shadow h-75'>
          <h2 className="fw-bold mb-2 text-uppercase bg-dark p-3 justify-content-center text-light">Login</h2>
          <Card.Body>
            <div className="mb-1 mt-md-2">
              <Form>
                <Form.Group className="mb-3" controlId="Email">
                  <Form.Control type="email" placeholder="Enter email"/>
                </Form.Group>
                <Form.Group className="mb-3" controlId="Password">
                  <Form.Control type="password" placeholder="Password"/>
                </Form.Group>
                <Form.Group className='col-md-12 text-center'>
                  <Button variant='outline-dark'
                          type="submit">Login</Button>
                </Form.Group>
              </Form>
            </div>
          </Card.Body>
        </Card>
      </Col>
    </Row>
  </Container>);
}

export default Login;