package openai4s.types

import cats.{Eq, Show}
import extras.render.Render
import io.circe.derivation.Configuration
import io.circe.derivation.*
import io.circe.*
import io.circe.{Codec, Decoder, Encoder}
import newtype4s.Newtype
import refined4s.strings.NonEmptyString

/** @author Kevin Lee
  * @since 2023-03-24
  */
final case class Message(role: Message.Role, content: Message.Content)
object Message {
  given messageConfiguration: Configuration = Configuration.default.withSnakeCaseMemberNames

  given messageEq: Eq[Message] = Eq.fromUniversalEquals

  given messageShow: Show[Message] = cats.derived.semiauto.show

  given messageCodec: Codec[Message] = ConfiguredCodec.derived

  type Role = Role.Type
  object Role extends Newtype[NonEmptyString] {

    given roleEq: Eq[Role] = Eq.fromUniversalEquals

    given roleRender: Render[Role] = Render.render(_.value.value)
    given roleShow: Show[Role]     = Show.show(_.value.value)

    given roleEncoder: Encoder[Role] = Encoder[String].contramap(_.value.value)
    given roleDecoder: Decoder[Role] = Decoder[String].emap(NonEmptyString.from).map(Role(_))
  }

  type Content = Content.Type
  object Content extends Newtype[NonEmptyString] {

    given contentEq: Eq[Content] = Eq.fromUniversalEquals

    given contentRender: Render[Content] = Render.render(_.value.value)
    given contentShow: Show[Content]     = Show.show(_.value.value)

    given contentEncoder: Encoder[Content] = Encoder[String].contramap(_.value.value)
    given contentDecoder: Decoder[Content] = Decoder[String].emap(NonEmptyString.from).map(Content(_))
  }

}
