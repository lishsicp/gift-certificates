import { useState } from "react";
import { useDispatch } from "react-redux";
import { addCertificate } from "../../../api/certificatesApi";
import { Form, Button, Modal, Row, Col, InputGroup, } from "react-bootstrap";
import { WithContext as ReactTags } from 'react-tag-input';

const AddCertificateModal = ({editCertificate = {}}) => {
  const dispatch = useDispatch();
  const [showModal, setShowModal] = useState(false);
  const [certificate, setCertificate] = useState(editCertificate);
  const [errors, setErrors] = useState({});
  const [tags, setTags] = useState([]);
  const [tagError, setTagError] = useState(null)
  const handleChange = (e) => {
    const { name, value } = e.target;
    setCertificate((prevCertificate) => ({
      ...prevCertificate,
      [name]: value,
    }));
  };

  const handleAddTag = (tag) => {
    if (tag.text && tag.text.length >= 3 && tag.text.length <= 50) {
      setTags([...tags, tag]);
      setTagError(null)
    } else {
      setTagError("Tag name must be between 3 and 50 characters")
    }
  };

  const handleRemoveTag = (index) => {
    const newTags = [...tags];
    newTags.splice(index, 1);
    setTags(newTags);
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    const validationErrors = validateForm();
    if (Object.keys(validationErrors).length === 0) {
      const certificateTags = tags.length > 0 ? tags.map(tag => ({ name: tag.text })) : [];
      const certificateData = {
        description: certificate.description,
        duration: certificate.duration,
        name: certificate.name,
        price: certificate.price,
        tags: certificateTags,
      };
      dispatch(addCertificate(certificateData, certificate.id !== undefined));
      handleCloseModal();
    } else {
      setErrors(validationErrors);
    }
  };

  const validateForm = () => {
    const errors = {};

    if (!certificate.name || certificate.name.trim() === "") {
      errors.name = "Title is required";
    } else if (certificate.name.length < 6 || certificate.name.length > 60) {
      errors.name = "Title must be between 6 and 60 characters";
    }

    if (!certificate.description || certificate.description.trim() === "") {
      errors.description = "Description is required";
    } else if (
      certificate.description.length < 12 ||
      certificate.description.length > 1000
    ) {
      errors.description = "Description must be between 12 and 1000 characters";
    }

    if (!certificate.price || isNaN(certificate.price) || parseFloat(certificate.price) <= 0) {
      errors.price = "Price must be a number greater than 0";
    }

    if (
      certificate.duration !== 0 &&
      (!certificate.duration || isNaN(certificate.duration))
    ) {
      errors.duration = "Duration must be a number";
    }

    if (tags.length > 0) {
      for (let i = 0; i < tags.length; i++) {
        const tagName = tags[i].text;
        if (tagName.length < 3 || tagName.length > 50) {
          errors.tags = "Tag name must be between 3 and 50 characters";
          break;
        }
      }
    }

    return errors;
  };

  const handleOpenModal = () => {
    setShowModal(true);
    if (editCertificate.id) {
      setCertificate(editCertificate);
      setTags(editCertificate.tags.map(tag => ({ id: tag.name, text: tag.name})))
    }
  };

  const handleDrag = (tag, currPos, newPos) => {
    const newTags = tags.slice();
    newTags.splice(currPos, 1);
    newTags.splice(newPos, 0, tag);
    setTags(newTags);
  };

  const handleCloseModal = () => {
    setShowModal(false);
    if (!certificate.id) {
      setCertificate({});
      setTags([]);
    }
    setTagError(null)
    setErrors({});
  };

  return (
    <>
      <Button
        variant={certificate.id ? "warning" : "primary"}
        size="sm"
        className="mx-0"
        onClick={handleOpenModal}
      >
        {certificate.id
          ? "Edit"
          : "Add New"}
      </Button>
      <Modal show={showModal} onHide={handleCloseModal} centered>
        <Modal.Header closeButton>
          <Modal.Title className="">
            {certificate.id
              ? `Edit certificate with ID = ${certificate.id}`
              : "Add New Certificate"}
          </Modal.Title>
        </Modal.Header>
        <Form onSubmit={handleSubmit}>
          <Modal.Body>
            <Form.Group className="mb-3" controlId="name" as={Row}>
              <Form.Label column sm={2} className="mx-2">
                Name
              </Form.Label>
              <Col>
                <Form.Control
                  type="text"
                  name="name"
                  value={certificate.name || ""}
                  onChange={handleChange}
                  isInvalid={!!errors.name}
                />
                <Form.Control.Feedback type="invalid">
                  {errors.name}
                </Form.Control.Feedback>
              </Col>
            </Form.Group>

            <Form.Group className="mb-3" controlId="description" as={Row}>
              <Form.Label column sm={2} className="mx-2">
                Description
              </Form.Label>
              <Col>
                <Form.Control
                  as="textarea"
                  rows={3}
                  name="description"
                  value={certificate.description || ""}
                  onChange={handleChange}
                  isInvalid={!!errors.description}
                />
                <Form.Control.Feedback type="invalid">
                  {errors.description}
                </Form.Control.Feedback>
              </Col>
            </Form.Group>

            <Form.Group className="mb-3" controlId="price" as={Row}>
              <Form.Label column sm={2} className="mx-2">
                Price
              </Form.Label>
              <Col>
                <Form.Control
                  type="number"
                  name="price"
                  value={certificate.price || ""}
                  onChange={handleChange}
                  isInvalid={!!errors.price}
                />
                <Form.Control.Feedback type="invalid">
                  {errors.price}
                </Form.Control.Feedback>
              </Col>
            </Form.Group>

            <Form.Group className="mb-3" controlId="duration" as={Row}>
              <Form.Label column sm={9} className="mx-2">
                Duration (in days)
              </Form.Label>
              <Col>
                <Form.Control
                  type="number"
                  name="duration"
                  value={certificate.duration || ""}
                  onChange={handleChange}
                  isInvalid={!!errors.duration}
                />
                <Form.Control.Feedback type="invalid">
                  {errors.duration}
                </Form.Control.Feedback>
              </Col>
            </Form.Group>

            <Form.Group className="mb-3" controlId="tags" as={Row}>
              <InputGroup className="mb-3">
                <Form.Label column sm={2} className="mx-2">
                  Tags
                </Form.Label>
                
                <ReactTags id="tag-input"
                  classNames={{
                    remove: "btn-sm text-decoration-none btn btn-link",
                    tags: "react-tags-wrapper col w-100",
                    selected: "mt-2",
                    tag: "pl-2 mx-1 text-truncate badge rounded-pill bg-dark",
                    tagInputField: "form-control",
                  }}
                  inputFieldPosition="top"
                  tags={tags}
                  handleDrag={handleDrag}
                  handleDelete={handleRemoveTag}
                  handleAddition={handleAddTag}
                  placeholder="Add tags.."
                ></ReactTags>
                <Form.Control
                hidden
                  type="text"
                  isInvalid={!!tagError}
                />
                {tagError ? (
                <Form.Control.Feedback type="invalid">
                  {tagError} 
                </Form.Control.Feedback>
                ) : null }
              </InputGroup>
            </Form.Group>
          </Modal.Body>
          <Modal.Footer className="justify-content-center align-content-center">
            <Row>
              <Col className="">
                <Button variant="primary" type="submit">
                  Save
                </Button>
              </Col>
              <Col className="">
                <Button variant="secondary" onClick={handleCloseModal}>
                  Close
                </Button>
              </Col>
            </Row>
          </Modal.Footer>
        </Form>
      </Modal>
    </>
  );
};

export default AddCertificateModal;
