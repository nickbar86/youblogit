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
  rows,
  rowsMax,
  placeholder,
  meta: { error, touched, warning }
}) => {
  return (
    <TextField
      {...input}
      error={error}
      helperText={error}
      required={required}
      fullWidth
      id="standard-multiline-flexible"
      label={label}
      autoComplete={autoComplete}
      autoFocus
      value={input.value}
      rows={rows}
      rowsMax={rowsMax}
      placeholder={placeholder}
      multiline
    />
  );
};
