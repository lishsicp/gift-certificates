import { React, useState, useEffect } from 'react';
import { fetchCertificates } from '../actions/actions';
import { LongTextPopup, formatDate, dateDiffInDays } from '../actions/utils';
import { Container, Table, Row, Col, Button, Badge } from 'react-bootstrap';

const Certificates = () => {
  return (
    <>
    <main>
      <Container className="d-flex justify-content-center align-items-center">
          <Row>
          <h2 className="fw-bold mt-2 text-uppercase bg-dark p-3 justify-content-center text-light">Gift Certificates</h2>
            <Col md={12} lg={12} xs={12}>
              <RenderCertificates />
            </Col>
          </Row>
      </Container>
    </main>
    </>
    );
  }

const RenderCertificates = () => {
  const [certificates, setCertificates] = useState([]);

  useEffect(() => {
    const fetchData = async () => {
      const data = await fetchCertificates(1, 10);
      setCertificates(data);
    };

    fetchData();
  }, []);

  const displayCertificates = () => {
    return (
        certificates &&
        certificates.map((certificate) => (
          <tr key={certificate.id}>
            <td className="align-middle">
              <LongTextPopup maxLength={25} text={certificate.name}/>
            </td>
            <td className="align-middle"><LongTextPopup maxLength={30} text={certificate.description}/></td>
            <td className="align-middle">{formatDate(certificate.createDate)}</td>
            <td className="align-middle">{certificate.price}</td>
            <td className="align-middle">{dateDiffInDays(certificate.createDate, certificate.duration) + ' days'}</td>
            <td className="align-middle align-middle w-25 text-center">
              <div className=''>
              {
                certificate.tags.map((tag) => (
                  <Badge pill bg="dark" className='m-1 p-2' key={tag.id}>
                    <LongTextPopup maxLength={15} text={tag.name.replace(/\s+\d+$/, '')} />
                  </Badge>
                ))
              }
              </div>
            </td>
            <td className="align-middle">
              <div className="row">
                <Button variant='danger' size='sm'>Delete</Button>
                <Button variant='warning' size='sm'>Edit</Button>
                <Button variant='info' size='sm'>View</Button>
              </div>
            </td>
          </tr>
        ))
    )
  }
  return (
    <Table striped hover>
      <thead>
        <tr>
          <th className="text-center">Name</th>
          <th className="text-center">Description</th>
          <th className="text-center">Creation Date</th>
          <th className="text-center">Price</th>
          <th className="text-center">Valid for</th>
          <th className="text-center">Tags</th>
          <th className="text-center">Actions</th>
        </tr>
      </thead>
      <tbody>
        { displayCertificates() }
      </tbody>
    </Table>
  );
};

export default Certificates;
