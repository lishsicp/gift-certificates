import {useState} from "react";
import {Form, Button, Modal} from "react-bootstrap";

const AddCertificateModal = () => {
  const [showModal, setShowModal] = useState(false);
  const [certificate, setCertificate] = useState({});

  const handleChange = (e) => {
    const {name, value} = e.target;
    setCertificate((prevCertificate) => ({
      ...prevCertificate,
      [name]: value,
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    // TODO
    handleCloseModal()
  };

  const handleOpenModal = () => {
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
            onClick={handleOpenModal}
        >
          Add certificate
        </Button>
        <Modal show={showModal} onHide={handleCloseModal} centered>
          <Modal.Header closeButton>
            <Modal.Title>Add new certificate</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <Form onSubmit={handleSubmit}>
              <Form.Group className="mb-3" controlId="name">
                <Form.Label>Name</Form.Label>
                <Form.Control
                    type="text"
                    name="name"
                    value={certificate.name || ""}
                    onChange={handleChange}
                />
              </Form.Group>

              <Form.Group className="mb-3" controlId="description">
                <Form.Label>Description</Form.Label>
                <Form.Control
                    as="textarea"
                    rows={3}
                    name="description"
                    value={certificate.description || ""}
                    onChange={handleChange}
                />
              </Form.Group>

              <Form.Group className="mb-3" controlId="price">
                <Form.Label>Price</Form.Label>
                <Form.Control
                    type="number"
                    name="price"
                    value={certificate.price || ""}
                    onChange={handleChange}
                />
              </Form.Group>

              <Form.Group className="mb-3" controlId="duration">
                <Form.Label>Duration (in days)</Form.Label>
                <Form.Control
                    type="number"
                    name="duration"
                    value={certificate.duration || ""}
                    onChange={handleChange}
                />
              </Form.Group>

              <Form.Group className="mb-3" controlId="tags">
                <Form.Label>Tags</Form.Label>
                <Form.Control
                    type="text"
                    name="tags"
                    value={(certificate.tags || []).map((tag) => tag.name).join(
                        ", ")}
                    onChange={handleChange}
                />
              </Form.Group>

              <Button variant="primary" type="submit">
                Save
              </Button>
            </Form>
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

export default AddCertificateModal;
