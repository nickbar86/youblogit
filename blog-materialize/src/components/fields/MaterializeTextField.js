import React from "react";
import TextField from "@material-ui/core/TextField";

export default ({
  input,
  label,
  name,
  type,
  autoComplete,
  id,
  required,
  meta: { error, touched, warning }
}) => {
  debugger
  return (
    <TextField
      {...input}
      error={error}
      helperText={error}
      variant="outlined"
      margin="normal"
      required={required}
      fullWidth
      id={id}
      label={label}
      autoComplete={autoComplete}
      autoFocus
      type={type}
      value={input.value}
    />
  );
};
