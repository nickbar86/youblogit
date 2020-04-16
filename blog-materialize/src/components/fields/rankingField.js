import React from "react";
import Ranking from "./../blog/ranking";

export default ({ input, name, type, id }) => {
  return (
    <Ranking
      {...input}
      id={id}
      ranking={input.value}
      onSelect={val => input.onChange(val)}
    />
  );
};
