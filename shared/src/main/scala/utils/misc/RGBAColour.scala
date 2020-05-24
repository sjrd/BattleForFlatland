package utils.misc

final case class RGBAColour(red: Int, green: Int, blue: Int, alpha: Double) extends Colour {

  def rgba: String = s"rgba($red,$green,$blue,$alpha)"

}
