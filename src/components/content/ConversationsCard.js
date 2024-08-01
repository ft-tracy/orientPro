import React, { useEffect, useRef, useState } from "react";
import { Card, Button, Form } from "react-bootstrap";
import ReactTimeAgo from "react-time-ago"; //For time ago display
import UserServices from "../../services/UserServices";

const ConversationsCard = ({ videoid, userid }) => {
  const [comments, setComments] = useState([]);
  const [newComment, setNewComment] = useState("");
  const [newReply, setNewReply] = useState({});
  const [showCommentButton, setShowCommentButton] = useState(false);
  const [showReplyButton, setShowReplyButton] = useState(false);
  const [showReplies, setShowReplies] = useState({});
  const [showMoreComments, setShowMoreComments] = useState(5);

  const inputRef = useRef(null);
  const buttonRef = useRef(null);

  // Fetch comments from database
  useEffect(() => {
    const fetchComments = async () => {
      try {
        const response = await UserServices.getComments(videoid);
        setComments(response);
        console.log("Fetched comments:", response);
      } catch (error) {
        console.error("Error fetching comments and replies:", error);
      }
    };
    fetchComments();
  }, [videoid]);

  const handleAddComment = async () => {
    if (newComment.trim()) {
      try {
        console.log(`comment text is:`, newComment);
        const response = await UserServices.addComment(
          userid,
          videoid,
          newComment
        ); // eslint-disable-line no-unused-vars
        const getResponse = await UserServices.getComments(videoid);
        const sortedTips = getResponse.sort(
          (a, b) => new Date(b.commentDate) - new Date(a.commentDate)
        );
        setComments(sortedTips);
        setNewComment("");
        setShowCommentButton(false);
      } catch (error) {
        console.error("Error adding a comment:", error);
      }
    }
  };

  const handleAddReply = async (commentId) => {
    if (newReply[commentId]?.trim()) {
      try {
        const response = await UserServices.addReply(
          userid,
          videoid,
          commentId,
          newReply[commentId]
        ); // eslint-disable-line no-unused-vars
        const getResponse = await UserServices.getComments(videoid);
        /* const sortedTips = getResponse.sort(
          (a, b) => new Date(b.replyDate) - new Date(a.replyDate)
        ); */
        setComments(getResponse);
        setNewReply({ ...newReply, [commentId]: "" });
        setShowReplies({ ...showReplies, [commentId]: true });
        setShowReplyButton(false);
      } catch (error) {
        console.error("Error adding reply:", error);
      }
    }
  };

  // Use star to collect and count number of likes
  const handleCommentLike = async (commentId) => {
    try {
      const updatedComment = comments.find(
        (comment) => comment.commentId === commentId
      );
      const updatedCommentLikes = updatedComment.likeCount + 1;
      console.log("Updated comment likes:", updatedCommentLikes);
      await UserServices.updateCommentLikes(
        videoid,
        commentId,
        updatedCommentLikes
      );
      setComments(
        comments.map((comment) =>
          comment.commentId === commentId
            ? { ...comment, likeCount: updatedCommentLikes }
            : comment
        )
      );
    } catch (error) {
      console.error("Error liking comment:", error);
    }
  };

  const handleReplyLike = async (commentId, replyId) => {
    try {
      const comment = comments.find(
        (comment) => comment.commentId === commentId
      );
      const reply = comment.replies.find((reply) => reply.replyId === replyId);
      const updatedReplyLikes = reply.likes + 1;
      await UserServices.updateReplyLikes(
        videoid,
        commentId,
        replyId,
        updatedReplyLikes
      );
      setComments(
        comments.map((comment) =>
          comment.commentId === commentId
            ? {
                ...comment,
                replies: comment.replies.map((reply) =>
                  reply.replyId === replyId
                    ? { ...reply, likeCount: updatedReplyLikes }
                    : reply
                ),
              }
            : comment
        )
      );
    } catch (error) {
      console.error("Error liking reply:", error);
    }
  };

  // Show more or less comments
  const handleShowMoreComments = () => {
    if (showMoreComments >= comments.length) {
      setShowMoreComments(5);
    } else {
      setShowMoreComments(showMoreComments + 10);
    }
  };

  const toggleReplies = (commentId) => {
    setShowReplies({
      ...showReplies,
      [commentId]: !showReplies[commentId],
    });
  };

  useEffect(() => {
    const handleClickOutside = (event) => {
      if (
        inputRef.current &&
        !inputRef.current.contains(event.target) &&
        buttonRef.current &&
        !buttonRef.current.contains(event.target)
      ) {
        setShowCommentButton(false);
        setShowReplies(false);
      }
    };

    document.addEventListener("mousedown", handleClickOutside);

    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, []);

  // Enter key handles button function
  const onKeyDownHandler = (e) => {
    if (e.keyCode === 13) {
      if (e.target.name === "newComment") {
        handleAddComment();
      } else if (e.target.name.startsWith("newReply")) {
        const commentId = e.target.name.split("-")[1];
        handleAddReply(commentId);
      }
    }
  };

  return (
    <>
      <Card className="comments-section">
        {/* General comments card */}
        <Card.Body>
          <Card.Title>Comments</Card.Title>
          <Form.Group controlId="newComment" className="comment-input-group">
            <Form.Control
              type="text"
              name="newComment"
              placeholder="Add a comment..."
              value={newComment}
              onFocus={() => setShowCommentButton(true)}
              onChange={(e) => setNewComment(e.target.value)}
              onKeyDown={onKeyDownHandler}
              className="comment-input"
              ref={inputRef}
            />
            {showCommentButton && (
              <Button
                type="submit"
                variant="primary"
                onClick={handleAddComment}
                className="add-comment-button"
                ref={buttonRef}
              >
                Add Comment
              </Button>
            )}
          </Form.Group>
          {/* End of add a comment section */}

          {/* Start of mapping comments and their replies */}
          {comments.slice(0, showMoreComments).map((comment) => (
            <div key={comment.commentId} className="comment-card">
              <Card className="comment-card">
                <Card.Body>
                  <div className="user-icon-container">
                    <div className="user-icon">
                      <span className="user-letter">{comment.userInitial}</span>
                    </div>
                    <div className="review-content">
                      <Card.Text>
                        <span>{comment.user}</span>
                        <span className="time-passed">
                          {comment.commentDate && (
                            <ReactTimeAgo
                              date={new Date(comment.commentDate)}
                              locale="en-US"
                            />
                          )}
                        </span>
                      </Card.Text>
                    </div>
                  </div>
                  <Card.Text>{comment.commentText}</Card.Text>

                  <div className="actions">
                    <div
                      className="likes"
                      onClick={() => handleCommentLike(comment.commentId)}
                    >
                      ⭐ {comment.likeCount}
                    </div>
                    <div
                      className="reply-icon"
                      onClick={() => toggleReplies(comment.commentId)}
                    >
                      💬
                    </div>
                  </div>

                  {comment.replies.length > 2 && (
                    <div className="show-more-replies">
                      <span onClick={() => toggleReplies(comment.commentId)}>
                        View all {comment.replies.length} replies
                      </span>
                    </div>
                  )}
                </Card.Body>
              </Card>

              {(showReplies[comment.commentId] ||
                comment.replies.length <= 2) && (
                <div className="replies-section">
                  {comment.replies.map((reply) => (
                    <Card key={reply.replyId} className="reply-card">
                      <Card.Body>
                        <div className="user-icon-container">
                          <div className="user-icon">
                            <span className="user-letter">
                              {reply.userInitial}
                            </span>
                          </div>
                          <div className="review-content">
                            <Card.Text>
                              <span>{reply.user}</span>
                              <span className="time-passed">
                                {reply.replyDate && (
                                  <ReactTimeAgo
                                    date={new Date(reply.replyDate)}
                                    locale="en-US"
                                  />
                                )}
                              </span>
                            </Card.Text>
                          </div>
                        </div>
                        <Card.Text>{reply.replyText}</Card.Text>
                        <div
                          className="likes"
                          onClick={() =>
                            handleReplyLike(comment.commentId, reply.replyId)
                          }
                        >
                          ⭐ {reply.likeCount}
                        </div>
                      </Card.Body>
                    </Card>
                  ))}
                </div>
              )}

              {showReplies[comment.commentId] && (
                <Form.Group
                  controlId={`newReply-${comment.commentId}`}
                  className="reply-input-group"
                >
                  <Form.Control
                    type="text"
                    placeholder="Write a reply..."
                    name={`newReply-${comment.commentId}`}
                    value={newReply[comment.commentId] || ""}
                    onFocus={() => setShowReplyButton(true)}
                    onChange={(e) =>
                      setNewReply({
                        ...newReply,
                        [comment.commentId]: e.target.value,
                      })
                    }
                    onKeyDown={onKeyDownHandler}
                    className="reply-input"
                    ref={inputRef}
                  />
                  {showReplyButton && (
                    <Button
                      type="submit"
                      onClick={() => handleAddReply(comment.commentId)}
                      className="add-reply-button"
                      ref={buttonRef}
                    >
                      Reply
                    </Button>
                  )}
                </Form.Group>
              )}
            </div>
          ))}

          {/* Show more or less comments */}
          {comments.length > 5 && (
            <div className="show-more-comments">
              <Button
                onClick={handleShowMoreComments}
                className="custom-button"
              >
                {showMoreComments >= comments.length
                  ? "Show less comments"
                  : "Show more comments"}
              </Button>
            </div>
          )}
        </Card.Body>
      </Card>
    </>
  );
};

export default ConversationsCard;
