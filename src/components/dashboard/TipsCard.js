import React, { useContext, useEffect, useRef, useState } from "react";
import { Button, Card, Form } from "react-bootstrap";
import ReactTimeAgo from "react-time-ago";
import UserServices from "../../services/UserServices";
import { AuthContext } from "../../contexts/AuthContext";

const TipsCard = ({ userInitials, userName }) => {
  const [tips, setTips] = useState([]);
  const [newTip, setNewTip] = useState("");
  const [showTipButton, setShowTipButton] = useState(false);
  const [showMoreTips, setShowMoreTips] = useState(2);

  const inputRef = useRef(null);
  const buttonRef = useRef(null);

  const { user } = useContext(AuthContext);

  useEffect(() => {
    const fetchTips = async () => {
      try {
        const response = await UserServices.getTips();
        console.log("Get tips:", response);
        const sortedTips = response.sort(
          (a, b) => new Date(b.tipDate) - new Date(a.tipDate)
        );
        setTips(sortedTips);
      } catch (error) {
        console.error("Error fetching tips:", error);
      }
    };
    fetchTips();
  }, []);

  const handleAddTip = async () => {
    console.log("handleAddTip called with newTip:", newTip);
    if (newTip.trim()) {
      // Add tip to database
      try {
        console.log("Sending request to add tip:", newTip);
        const response = await UserServices.addTips(user?.documentId, newTip);
        console.log("Add tips response:", response);
        const addResponse = await UserServices.getTips();
        const sortedTips = addResponse.sort(
          (a, b) => new Date(b.tipDate) - new Date(a.tipDate)
        );
        setTips(sortedTips);
        setNewTip("");
        setShowTipButton(false);
      } catch (error) {
        console.error("Error adding a tip:", error);
      }
    } else {
      console.log("Tip is empty, not sending request.");
    }
  };

  const handleShowMoreTips = () => {
    if (showMoreTips >= tips.length) {
      setShowMoreTips(2);
    } else {
      setShowMoreTips(showMoreTips + 10);
    }
  };

  useEffect(() => {
    const handleClickOutside = (event) => {
      if (
        inputRef.current &&
        !inputRef.current.contains(event.target) &&
        buttonRef.current &&
        !buttonRef.current.contains(event.target)
      ) {
        setShowTipButton(false);
      }
    };

    document.addEventListener("mousedown", handleClickOutside);

    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, []);

  const onKeyDownHandler = (e) => {
    if (e.keyCode === 13) {
      if (e.target.name === "newTip") {
        handleAddTip();
      }
    }
  };

  return (
    <>
      <Card className="comments-section">
        <Card.Body>
          <Card.Title>Helpful Tips</Card.Title>
          <Form.Group controlId="newTip" className="comment-input-group">
            <Form.Control
              type="text"
              name="newTip"
              placeholder="Enter a tip"
              value={newTip}
              onFocus={() => setShowTipButton(true)}
              onChange={(e) => {
                setNewTip(e.target.value);
              }}
              onKeyDown={onKeyDownHandler}
              className="comment-input"
              ref={inputRef}
            />
            {showTipButton && (
              <Button
                type="button"
                variant="primary"
                onClick={() => {
                  console.log("Add Tip button clicked");
                  handleAddTip();
                }}
                className="add-comment-button"
                ref={buttonRef}
              >
                Add Tip
              </Button>
            )}
          </Form.Group>

          {tips.slice(0, showMoreTips).map((tip) => {
            const tipUserName = `${tip.firstName} ${tip.lastName}`;
            const tipUserInitials = `${tip.firstName[0]}${tip.lastName[0]}`;

            return (
              <div key={tip.tipId} className="conversation-card">
                <Card className="comment-card">
                  <Card.Body>
                    <div className="user-icon-container">
                      <div className="user-icon">
                        <span className="user-letter">{tipUserInitials}</span>
                      </div>
                      <div className="review-content">
                        <Card.Text>
                          <span>{tipUserName}</span>
                          <span className="time-passed">
                            {tip.tipDate && (
                              <ReactTimeAgo
                                date={new Date(tip.tipDate)}
                                locale="en-US"
                              />
                            )}
                          </span>
                        </Card.Text>
                      </div>
                    </div>
                    <Card.Text>{tip.tipContent}</Card.Text>
                  </Card.Body>
                </Card>
              </div>
            );
          })}

          {tips.length > 2 && (
            <div className="show-more-conversations">
              <Button onClick={handleShowMoreTips} className="custom-button">
                {showMoreTips >= tips.length
                  ? "Show less tips"
                  : "Show more tips"}
              </Button>
            </div>
          )}
        </Card.Body>
      </Card>
    </>
  );
};

export default TipsCard;
