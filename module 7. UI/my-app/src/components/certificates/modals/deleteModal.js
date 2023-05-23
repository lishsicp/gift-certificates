import {useState} from "react";
import {Modal, Button} from "react-bootstrap";
import { useDispatch } from "react-redux";
import { deleteCertificate } from "../../../api/certificatesApi"

const DeleteModal = ({id, name}) => {
  const [showDeleteModal, setShowDeleteModal] = useState(false);
  const dispatch = useDispatch();

  const openDeleteModal = () => {
    setShowDeleteModal(true);
  };

  const closeDeleteModal = () => {
    setShowDeleteModal(false);
  };
  const handleDelete = () => {
    dispatch(deleteCertificate(id))
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
              /Gift Certificate \d*$/, "").trim()} certificate? (id = {id})
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
