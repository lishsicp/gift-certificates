import {useState} from "react";
import {Modal, Button, Badge} from "react-bootstrap";
import {
  formatDate,
  dateDiffInDays,
} from "../../utils";

const ViewModal = ({certificate}) => {
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

        <Modal show={showModal} onHide={handleCloseModal} centered size="lg">
          <Modal.Header closeButton>
            <Modal.Title>Certificate Details</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            {/* Render the detailed information of the selected certificate */}
            {selectedCertificate && (
                <>
                  <h4>{selectedCertificate.name.replace(/Gift Certificate \d*$/,
                      "").trim()}</h4>
                  <p>{selectedCertificate.description}</p>
                  {dateDiffInDays(selectedCertificate.createDate,
                      selectedCertificate.duration) + " days"}
                  <p>{formatDate(selectedCertificate.createDate)}</p>
                  <p>{formatDate(selectedCertificate.lastUpdateDate)}</p>
                  <p>{selectedCertificate.price}</p>
                  <p>{selectedCertificate.description}</p>
                  {selectedCertificate.tags.map((tag, tagIndex) => (
                      <Badge
                          pill
                          bg="dark"
                          className="m-1 p-2 text-truncate"
                          key={tagIndex}
                      >

                        {tag.name.replace(/\s+\d+$/, "")}

                      </Badge>
                  ))}
                  {/* Add more fields or information as needed */}
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
