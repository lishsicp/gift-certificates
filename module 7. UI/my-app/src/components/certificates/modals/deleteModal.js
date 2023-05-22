import {useState} from "react";
import {Modal, Button} from "react-bootstrap";

const DeleteModal = ({id, name}) => {
  const [showDeleteModal, setShowDeleteModal] = useState(false);

  const openDeleteModal = () => {
    setShowDeleteModal(true);
  };

  const closeDeleteModal = () => {
    setShowDeleteModal(false);
  };
  const handleDelete = () => {
    // TODO
    closeDeleteModal();
  };

  return (
      <>
        <Button
            variant="danger"
            size="sm"
            className="mx-0"
            onClick={openDeleteModal}
        >
          Delete
        </Button>
        <Modal show={showDeleteModal} onHide={closeDeleteModal} centered>
          <Modal.Header closeButton>
            <Modal.Title>Confirm Delete</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            Are you sure you want to delete {name.replace(
              /Gift Certificate \d*$/, "").trim()} certificate?
          </Modal.Body>
          <Modal.Footer>
            <Button variant="secondary" onClick={closeDeleteModal}>
              Cancel
            </Button>
            <Button variant="danger" onClick={handleDelete}>
              Delete
            </Button>
          </Modal.Footer>
        </Modal>
      </>
  );
};

export default DeleteModal;
