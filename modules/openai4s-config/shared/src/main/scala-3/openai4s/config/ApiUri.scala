package openai4s.config

import cats.{Eq, Show}
import cats.syntax.all.*
import extras.render.Render
import extras.render.syntax.*
import pureconfig.*
import pureconfig.generic.derivation.default.*
import pureconfig.error.CannotConvert
import refined4s.types.all.*

/** @author Kevin Lee
  * @since 2023-04-07
  */
final case class ApiUri(baseUri: ApiUri.BaseUri) derives ConfigReader
object ApiUri {

  val default: ApiUri = ApiUri.fromUri(CommonConstants.DefaultOpenAiUri)

  def fromUri(uri: Uri): ApiUri = ApiUri(BaseUri(uri))

  given apiUriEq: Eq[ApiUri] = Eq.fromUniversalEquals

  given apiUriShow: Show[ApiUri] = Show.fromToString

  given apiUriRender: Render[ApiUri] = Render[BaseUri].contramap(_.baseUri)

  extension (apiUri: ApiUri) {
    def chatCompletions: NonEmptyString =
      NonEmptyString.unsafeFrom(render"${apiUri.baseUri}/v1/chat/completions")

    def completions: NonEmptyString =
      NonEmptyString.unsafeFrom(render"${apiUri.baseUri}/v1/completions")
  }

  type BaseUri = BaseUri.BaseUri
  object BaseUri {
    opaque type BaseUri = Uri
    def apply(baseUri: Uri): BaseUri = baseUri

    given baseUriCanEqual: CanEqual[BaseUri, BaseUri] = CanEqual.derived

    extension (baseUri: BaseUri) {
      def value: Uri = baseUri
    }

    given baseUriEq: Eq[BaseUri] = Eq.fromUniversalEquals

    given baseUriRender: Render[BaseUri] = Render.stringRender.contramap(_.value.value)
    given baseUriShow: Show[BaseUri]     = Show.catsShowForString.contramap(_.value.value)

    given baseUriConfigReader: ConfigReader[BaseUri] = ConfigReader
      .stringConfigReader
      .emap(s => Uri.from(s).leftMap(err => CannotConvert(s, "refined4s.types.all.Uri", err)))

  }

}
