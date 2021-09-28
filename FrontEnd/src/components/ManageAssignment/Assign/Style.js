import { makeStyles } from "@material-ui/core/styles";
export default makeStyles((theme) => ({
  dialogContent: {
    backgroundColor: "#FDFEFE",
    display: "flex",
    padding: 40,
    paddingRight: 200,
    justifyContent: "space-around",
  },
  dialogTitle: {
    color: "red",
    backgroundColor: "#FDFEFE",
    border: 1,
    borderRadius: 5,
  },
  title: {
    marginRight: 40,
  },
}));
