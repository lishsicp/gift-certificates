import { useState } from "react";
import { Modal, Button, Badge, Col, Row } from "react-bootstrap";
import { formatDate, dateDiffInDays } from "../../utils";

const ViewModal = ({ certificate }) => {
  const [showModal, setShowModal] = useState(false);
  const [selectedCertificate, setSelectedCertificate] = useState(null);

  const handleOpenModal = () => {
    setSelectedCertificate(certificate);
    setShowModal(true);
  };

  const handleCloseModal = () => {
    setShowModal(false);
  };

  return (
    <>
      <Button
        variant="info"
        size="sm"
        className="mx-0"
        onClick={() => handleOpenModal()}
      >
        View
      </Button>

      <Modal show={showModal} onHide={handleCloseModal} centered>
        <Modal.Header closeButton>
          <Modal.Title>Certificate Details</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          {selectedCertificate && (
            <>
              <Row className=" ">
                <Col xs={6} md={4}>
                  <p className="fs-5 fw-bold">Name:</p>
                </Col>
                <Col xs={6} md={8}>
                  <p className="fs-5">
                    {selectedCertificate.name
                      .replace(/Gift Certificate \d*$/, "")
                      .trim()}
                  </p>
                </Col>
              </Row>
              <Row className=" ">
                <Col xs={6} md={4}>
                  <p className="fs-5 fw-bold">Description:</p>
                </Col>
                <Col xs={6} md={8}>
                  <p className="fs-5">{selectedCertificate.description}</p>
                </Col>
              </Row>
              <Row className=" ">
                <Col xs={6} md={4}>
                  <p className="fs-5 fw-bold">Duration:</p>
                </Col>
                <Col xs={6} md={8}>
                  <p className="fs-5">
                    {dateDiffInDays(
                      selectedCertificate.createDate,
                      selectedCertificate.duration
                    )}
                  </p>
                </Col>
              </Row>
              <Row className=" ">
                <Col xs={6} md={4}>
                  <p className="fs-5 fw-bold">Create Date:</p>
                </Col>
                <Col xs={6} md={8}>
                  <p className="fs-5">
                    {formatDate(selectedCertificate.createDate)}
                  </p>
                </Col>
              </Row>
              <Row className=" ">
                <Col xs={6} md={4}>
                  <p className="fs-5 fw-bold">Last Update:</p>
                </Col>
                <Col xs={6} md={8}>
                  <p className="fs-5">
                    {formatDate(selectedCertificate.lastUpdateDate)}
                  </p>
                </Col>
              </Row>
              <Row className=" ">
                <Col xs={6} md={4}>
                  <p className="fs-5 fw-bold">Price:</p>
                </Col>
                <Col xs={6} md={8}>
                  <p className="fs-5">{selectedCertificate.price}</p>
                </Col>
              </Row>
              <Row className=" ">
                <Col xs={6} md={4}>
                  <p className="fs-5 fw-bold">Tags:</p>
                </Col>
                <Col xs={6} md={8}>
                  <div>
                    {selectedCertificate.tags.map((tag, tagIndex) => (
                      <Badge
                        pill
                        bg="dark"
                        className="m-1 p-2 text-truncate fs-6"
                        key={tagIndex}
                      >
                        {tag.name.replace(/\s+\d+$/, "")}
                      </Badge>
                    ))}
                  </div>
                </Col>
              </Row>
            </>
          )}
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={handleCloseModal}>
            Close
          </Button>
        </Modal.Footer>
      </Modal>
    </>
  );
};

export default ViewModal;
