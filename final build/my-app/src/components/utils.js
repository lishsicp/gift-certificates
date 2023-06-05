import { Alert, OverlayTrigger, Tooltip } from "react-bootstrap";
import React, { useEffect, useState } from "react";
import { ExclamationOctagonFill, CheckCircle } from "react-bootstrap-icons";

export function LongTextPopup({ text, maxLength }) {
  if (!maxLength) {
    maxLength = 30;
  }
  if (text.length > maxLength) {
    return (
      <OverlayTrigger
        key="popup"
        trigger={["hover", "focus"]}
        placement="right"
        overlay={<Tooltip id="tooltip-long-text">{text}</Tooltip>}
      >
        <span>
          {text.length > maxLength ? text.slice(0, maxLength) + "..." : text}
        </span>
      </OverlayTrigger>
    );
  } else {
    return <span>{text}</span>;
  }
}

export function formatDate(dateString) {
  const options = {
    day: "2-digit",
    month: "short",
    year: "numeric",
    hour: "2-digit",
    minute: "2-digit",
  };
  const date = new Date(dateString);
  return date.toLocaleDateString("en-US", options);
}

export function dateDiffInDays(createDate, durationInDays) {
  const _MS_PER_DAY = 1000 * 60 * 60 * 24;

  const startDate = new Date(createDate);
  const expirationDate = new Date(
    startDate.getTime() + durationInDays * _MS_PER_DAY
  );
  const currentDate = new Date();
  const difference = expirationDate.getTime() - currentDate.getTime();
  const days = Math.ceil(difference / _MS_PER_DAY);
  let result;

  if (days === 1) {
    result = days + " day";
  } else if (days < 0) {
    result = "expired";
  } else if (days === 0) {
    result = "unlimited";
  } else {
    result = days + " days";
  }
  return result;
}

export const DismissibleAlert = ({ color, message, reset }) => {
  const [show, setShow] = useState(true);
  const colors = [
    "primary",
    "secondary",
    "success",
    "danger",
    "warning",
    "info",
    "light",
    "dark",
    "muted",
    "white",
  ];
  if (!colors.includes(color)) {
    throw new Error(`Color ${color} is not supported`);
  }

  useEffect(() => {
    if (message && reset) {
      const timeoutId = setTimeout(() => {
        setShow(false);
      }, 15000);
      return () => clearTimeout(timeoutId);
    }
  }, [reset, message]);

  if (show) {
    return (
      <Alert
        variant={color}
        className="mt-2"
        onClose={() => setShow(false)}
        dismissible
      >
        <Alert.Heading className="d-flex align-items-center">
          {color === "danger" ? (
            <ExclamationOctagonFill className="mx-2" />
          ) : (
            <CheckCircle className="mx-2" />
          )}
          {message}
        </Alert.Heading>
      </Alert>
    );
  }
};
