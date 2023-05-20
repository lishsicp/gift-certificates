import {Alert, OverlayTrigger, Tooltip} from 'react-bootstrap';
import React, {useState} from 'react';

export function LongTextPopup({text, maxLength}) {
  if (!maxLength) {
    maxLength = 30;
  }
  if (text.length > maxLength) {
    return (<OverlayTrigger
            key="popup"
            trigger={['hover', 'focus']}
            placement="right"
            overlay={<Tooltip id='tooltip-long-text'>{text}</Tooltip>}>
          <span>{text.length > maxLength ? text.slice(0, maxLength) + '...'
              : text}</span>
        </OverlayTrigger>);
  } else {
    return <span>{text}</span>;
  }
}

export function formatDate(dateString) {
  const options = {
    day: '2-digit',
    month: 'short',
    year: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  };
  const date = new Date(dateString);
  return date.toLocaleDateString('en-US', options);
}

export function dateDiffInDays(createDate, durationInDays) {
  const _MS_PER_DAY = 1000 * 60 * 60 * 24;

  const startDate = new Date(createDate);
  const expirationDate = new Date(
      startDate.getTime() + durationInDays * _MS_PER_DAY);
  const currentDate = new Date();

  const difference = expirationDate.getTime() - currentDate.getTime();

  return Math.ceil(difference / (_MS_PER_DAY));
}

export function DismissibleError({errorText}) {
  const [show, setShow] = useState(true);

  if (show) {
    return (
        <Alert variant="danger" className='mt-2' onClose={() => setShow(false)}
               dismissible>
          <Alert.Heading>Oh snap! You got an error!</Alert.Heading>
          <p>
            {errorText}
          </p>
        </Alert>);
  }
}