import React, { Component } from "react";
import { connect } from "react-redux";
import { EditorState } from "draft-js";
import { Editor } from "react-draft-wysiwyg";
import "react-draft-wysiwyg/dist/react-draft-wysiwyg.css";
import TextField from "@material-ui/core/TextField";
import Button from "@material-ui/core/Button";
import "./editorStyles.css";
import Typography from "@material-ui/core/Typography";

class PostForm extends Component {
  componentDidMount() {
    if (this.props.isNew) {
      this.props.initPostContentState(EditorState.createEmpty());
    }
  }

  onChange = editorState => {
    this.props.updatePostContent(editorState);
  };

  render() {
    return (
      <div>
        <Typography variant="h4" color="inherit" gutterBottom>
          Write your story:)
        </Typography>
        <div>
          <TextField
            required
            id="standard-required"
            label="Title"
            placeholder="Add a title"
            value={this.props.post.title}
            onChange={this.props.updatePostTitle}
            fullWidth={true}
          />
          <TextField
            id="standard-multiline-flexible"
            label="Summary"
            multiline
            rowsMax="4"
            rows="4"
            onChange={this.props.updatePostSummary}
            placeholder="Add a summary"
            value={this.props.post.summary}
            fullWidth={true}
          />
        </div>
        <div className={"editor"}>
          {this.props.post.content && (
            <Editor
              wrapperClassName="wrapper-class"
              editorClassName="editor-class"
              toolbarClassName="toolbar"
              toolbar={{
                inline: { inDropdown: true },
                list: { inDropdown: true },
                textAlign: { inDropdown: true },
                link: { inDropdown: true },
                history: {},
                embedded: {},
                image: {
                  inDropdown: true,
                  urlEnabled: true,
                  uploadEnabled: true,
                  alignmentEnabled: true,
                  previewImage: true,
                  uploadCallback: this.uploadCallback
                },
                remove: {}
              }}
              onEditorStateChange={this.onChange}
              editorState={this.props.post.content}
              spellCheck={true}
              placeholder="Add your story"
            />
          )}
        </div>

        <Button
          variant="contained"
          onClick={() => this.props.savePost(this.props.post)}
        >
          Save
        </Button>
      </div>
    );
  }
}

function mapStateToPros() {
  return {};
}

export default connect(
  mapStateToPros,
  {}
)(PostForm);
