import React from "react";
import { Container } from "react-bootstrap";
/* import ReactHtmlParser from "react-html-parser"; */

const ReadingContentPage = ({ content, courseid }) => {
  const uploadedBy = content.firstName + content.lastName;

  if (!content) {
    return <div>Loading...</div>;
  }

  return (
    <>
      <Container className="reading-content-container">
        <div className="titleContainer">{content.readingTitle}</div>

        <div
          className="reading-container"
          dangerouslySetInnerHTML={{ __html: content.readingContent }}
        >
          {/* {ReactHtmlParser(content.readingContent)} */}
        </div>

        <div className="uploaded-by">{uploadedBy}</div>
      </Container>
    </>
  );
};

export default ReadingContentPage;
