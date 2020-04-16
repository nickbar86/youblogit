import React from "react";
import PropTypes from "prop-types";
import StarIcon from "@material-ui/icons/Star";
import StarBorderIcon from "@material-ui/icons/StarBorder";
import StarHalfIcon from "@material-ui/icons/StarHalf";

function getStar(min, max, rank, onSelect) {
  if (!rank) {
    return (
      <StarBorderIcon
        style={{ color: "#3f51b5", cursor: "pointer" }}
        onClick={() => onSelect(max)}
      />
    );
  }
  if (rank <= min)
    return (
      <StarBorderIcon
        onClick={() => onSelect(max)}
        style={{ color: "#3f51b5", cursor: "pointer" }}
      />
    );
  else if (rank >= min && rank < max) {
    return (
      <StarHalfIcon
        onClick={() => onSelect(max)}
        style={{ color: "#3f51b5", cursor: "pointer" }}
      />
    );
  } else {
    return (
      <StarIcon
        onClick={() => onSelect(max)}
        style={{ color: "#3f51b5", cursor: "pointer" }}
      />
    );
  }
}
export default function Ranking(props) {
  const { onSelect, ranking } = props;
  return (
    <>
      {getStar(0, 1, ranking, onSelect)}
      {getStar(1, 2, ranking, onSelect)}
      {getStar(2, 3, ranking, onSelect)}
      {getStar(3, 4, ranking, onSelect)}
      {getStar(4, 5, ranking, onSelect)}
    </>
  );
}

Ranking.propTypes = {
  ranking: PropTypes.number,
  onSelect: PropTypes.func
};
