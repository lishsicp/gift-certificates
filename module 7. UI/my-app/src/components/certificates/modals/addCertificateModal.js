import { useState } from "react";
import { useDispatch } from "react-redux";
import { Form, Button, Modal, Row, Col, InputGroup } from "react-bootstrap";
import { WithContext as ReactTags } from "react-tag-input";
import "./tag-input.css";
import {
  useAddCertificateMutation,
  useEditCertificateMutation,
} from "../../../api/certificatesApiMutations";
import {
  addCertificateFailure,
  addCertificateSuccess,
} from "../../../features/certificate/addCertificateSlice";

const AddCertificateModal = ({ certificateToEdit = {}, onClick }) => {
  const dispatch = useDispatch();
  const [showModal, setShowModal] = useState(false);
  const [certificate, setCertificate] = useState(certificateToEdit);
  const [errors, setErrors] = useState({});
  const [tags, setTags] = useState([]);
  const [tagError, setTagError] = useState(null);
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
      setTagError(null);
    } else {
      setTagError("Tag must be between 3 and 50 characters");
    }
  };

  const handleRemoveTag = (index) => {
    const newTags = [...tags];
    newTags.splice(index, 1);
    setTags(newTags);
  };

  const [add] = useAddCertificateMutation();
  const [edit] = useEditCertificateMutation();

  const handleSubmit = (e) => {
    e.preventDefault();
    const validationErrors = validateForm();
    if (Object.keys(validationErrors).length === 0) {
      const certificateTags =
        tags.length > 0 ? tags.map((tag) => ({ name: tag.text })) : [];
      const certificateData = {
        ...(certificate.id && { id: certificate.id }),
        description: certificate.description,
        duration: certificate.duration,
        name: certificate.name,
        price: certificate.price,
        tags: certificateTags,
      };

      if (certificate.id === null || certificate.id === undefined) {
        add(certificateData)
          .unwrap()
          .then((response) => {
            dispatch(addCertificateSuccess(response.data));
          })
          .catch((error) => {
            dispatch(addCertificateFailure(error.data));
          });
      } else {
        const id = certificate?.id;
        edit({ id, ...certificateData })
          .unwrap()
          .then((response) => {
            dispatch(addCertificateSuccess(response.data));
          })
          .catch((error) => {
            dispatch(addCertificateFailure(error.data));
          });
      }
      onClick();
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

    if (
      !certificate.price ||
      isNaN(certificate.price) ||
      parseFloat(certificate.price) <= 0
    ) {
      errors.price = "Price must be a number greater than 0";
    }

    if (
      certificate.duration !== 0 &&
      (!certificate.duration || isNaN(certificate.duration))
    ) {
      errors.duration = "Duration must be a number";
    }

    return errors;
  };

  const handleOpenModal = () => {
    setShowModal(true);
    if (certificateToEdit.id) {
      setCertificate(certificateToEdit);
      setTags(
        certificateToEdit.tags.map((tag) => ({ id: tag.name, text: tag.name }))
      );
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
    setTagError(null);
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
        {certificate.id ? "Edit" : "Add New"}
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
                  autoFocus
                  autoComplete="false"
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
                  Enter tags
                </Form.Label>
                <ReactTags
                  id="tags"
                  classNames={{
                    remove: "btn-sm text-decoration-none btn bg-transparent",
                    tags: "react-tags-wrapper col w-100",
                    selected: "mt-2 align-items-center justify-content-center",
                    tag: "pl-2 mx-1 text-truncate badge bg-light text-dark item",
                    tagInputField: "form-control",
                  }}
                  autofocus={false}
                  inputFieldPosition="top"
                  tags={tags}
                  handleDrag={handleDrag}
                  handleDelete={handleRemoveTag}
                  handleAddition={handleAddTag}
                  placeholder="Press `Enter` to add tag..."
                ></ReactTags>
                <Form.Control hidden type="text" isInvalid={!!tagError} />
                {tagError ? (
                  <Form.Control.Feedback type="invalid" className="text-center">
                    {tagError}
                  </Form.Control.Feedback>
                ) : null}
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
